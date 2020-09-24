package com.zyh.hu.service.hu;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.zyh.hu.comments.ResponseBaseBean;
import com.zyh.hu.comments.ResponseStatusEnum;
import com.zyh.hu.entity.SysUser;
import com.zyh.hu.repository.UserRepository;
import com.zyh.hu.service.BaseService;
import com.zyh.hu.utils.HttpClientUtil;
import com.zyh.hu.utils.ProductCodesUtil;
import com.zyh.hu.utils.StringsUtil;

/**
 * 
 * 通过接口发送文件
 * @Description:
 * @author HU
 * @date 2020年7月10日
 */
@Service("HU100003Service")
public class HU100003Service implements BaseService {
	private static final Logger logger = LoggerFactory.getLogger(HU100003Service.class);
	private static final String TRANSCODE = "HU100003";

	@Value("${SEND.FILE.URL}")
	private String sendFileUrl;
	
	@Value("${SEND.FILE.ROUTE}")
	private String sendFileRoute;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public ResponseBaseBean doExecute(JSONObject jsonO,
			HttpServletRequest request, HttpServletResponse response,
			String uuid) {
		ResponseBaseBean baseBean = new ResponseBaseBean();
		baseBean.setTransCode(TRANSCODE);
		
		String userId = String.valueOf(jsonO.get("userId"));//用户ID
		String fileName = String.valueOf(jsonO.get("fileName"));//文件名
		String baserialNo = String.valueOf(jsonO.get("baserialNo"));//申请号
		String policyNo = String.valueOf(jsonO.get("policyNo"));//保单号
		
		if (StringsUtil.isNull(fileName) || StringsUtil.isNull(baserialNo)
				|| StringsUtil.isNull(policyNo) || StringsUtil.isNull(userId)) {
			baseBean.initStatus(ResponseStatusEnum.RESPONSE_BICHUAN_PARAMES_EMPTOY);
			return baseBean;
		}
		
		SysUser user = userRepository.findById(Integer.valueOf(userId));
		if (null == user) {
			baseBean.initStatus(ResponseStatusEnum.RESPONSE_USER_ISNOT_FIND);
			return baseBean;
		}
		 
		String result = "";
		File file = new File(sendFileRoute+fileName);
		if (file.exists()) {
			result = HttpClientUtil.postFile(sendFileUrl, policyNo, file);
		}
		logger.info("---上传文件返回值-HU100003:" + result);
		if (ProductCodesUtil.SUCCESS_CODE.equals(result)) {
			baseBean.initStatus(ResponseStatusEnum.RESPONSE_SUCCESS);
		}
		return baseBean;
	}

}
