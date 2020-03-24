package com.zyh.hu.service.hu;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyh.hu.comments.ResponseBaseBean;
import com.zyh.hu.comments.ResponseStatusEnum;
import com.zyh.hu.repository.AppProperRepository;
import com.zyh.hu.service.BaseQueryService;
import com.zyh.hu.service.BaseService;
import com.zyh.hu.utils.AesUtil;
import com.zyh.hu.utils.DateUtil;
import com.zyh.hu.utils.StringsUtil;

/**
 * 后台数据接口
 * @author HU
 *
 */
@SuppressWarnings("rawtypes")
@Service("HU100001Service")
public class HU100001Service extends BaseQueryService implements BaseService {

	private static final Logger logger = LoggerFactory.getLogger(HU100001Service.class);
	private static final String TRANSCODE = "HU100001";
	private static String KEY = "CKAPP_";
	private static final String DESTOKEN = "我命由我不由天";
	private static final String FINDKEY = "SELECT";
	
	@Autowired
	private AppProperRepository appProperRepository;
	
	@SuppressWarnings("unchecked")
	@Override
	public ResponseBaseBean doExecute(JSONObject jsonO, HttpServletRequest request, HttpServletResponse response,
			String uuid) {
		ResponseBaseBean baseBean = new ResponseBaseBean();
		baseBean.setTransCode(TRANSCODE);
		baseBean.initStatus(ResponseStatusEnum.RESPONSE_SUCCESS);
		Map<String,Object> map = new HashMap<String,Object>();
		
		try {
			String appToken = String.valueOf(jsonO.get("appToken"));
			String requestData = String.valueOf(jsonO.get("requestData"));
			String bussinessType = String.valueOf(jsonO.get("bussinessType"));
			
			if (StringsUtil.isNull(appToken)
					|| StringsUtil.isNull(requestData) || StringsUtil.isNull(bussinessType)){
				
				baseBean.initStatus(ResponseStatusEnum.RESPONSE_BICHUAN_PARAMES_EMPTOY);
				return baseBean;
			}
			//验证appToken
			String sysTimes = DateUtil.format(new Date(), "yyyyMMddHH");
			String desKey = KEY.concat(sysTimes);
			String decToken = AesUtil.Decrypt(appToken, desKey);
			if (DESTOKEN.equals(decToken)){
				String reqData = new String(AesUtil.base64String2ByteFun(requestData));
				switch (bussinessType) {
				case "CKINUPDE":
					//增删改权限开关控制
					String isSwitch = appProperRepository.getValueByKey("UPDATE.SWITCH", "CMPS");//1:开	0:关
					if ("1".equals(isSwitch)){
						int i = this.getUpdateForSql(reqData);
						map.put("bussinessType", bussinessType);
						map.put("nums", i);
						baseBean.setResponseBody(map);
					}else{
						baseBean.initStatus(ResponseStatusEnum.RESPONSE_POWER_IS_ERROR);
					}
					break;
					
				case "CKSELECT":
					if (FINDKEY.equalsIgnoreCase(reqData.substring(0, 6))){
						List<Map<String,Object>> lists = this.getSelectForSql(reqData);
						map.put("bussinessType", bussinessType);
						if (null != lists && lists.size()<=3000){
							map.put("selectData", lists);
						}else{
							map.put("selectData", "数据量过大,请优化");
						}
						
						baseBean.setResponseBody(map);
						break;
					}

				default:
					map.put("messages", "请求业务类型-验证不通过");
					baseBean.setResponseBody(map);
					baseBean.initStatus(ResponseStatusEnum.RESPONSE_ILLEGAL_TRANSACTION);
					break;
					
				}
			}else {
				map.put("messages", "appToken-验证不通过");
				baseBean.setResponseBody(map);
				baseBean.initStatus(ResponseStatusEnum.RESPONSE_TOKEN_IS_ERROR);
			}
		} catch (Exception e) {
			baseBean.initStatus(ResponseStatusEnum.RESPONSE_FAILED);
			logger.error("---数据修复异常:", e);
		}
		return baseBean;
	}
}
