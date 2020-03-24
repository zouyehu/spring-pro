package com.zyh.hu.service.hu;

import java.util.Date;
import java.util.HashMap;
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
import com.zyh.hu.entity.SysUser;
import com.zyh.hu.repository.UserRepository;
import com.zyh.hu.service.BaseService;
import com.zyh.hu.service.SysErrorLogSystemService;
import com.zyh.hu.service.responsexml.SHResponse;
import com.zyh.hu.service.utils.RequestUtil;
import com.zyh.hu.utils.StringsUtil;

/**
 * 模拟换卡接口业务实现
 * @author HU
 *
 */
@Service("HU100002Service")
public class HU100002Service implements BaseService{

	private static final Logger logger = LoggerFactory.getLogger(HU100002Service.class);
	private static final String TRANSCODE = "HU100002";
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private SysErrorLogSystemService sysErrorLogSystemService;
	
	@Autowired
	private RequestUtil requestUtil;
	
	@Override
	public ResponseBaseBean doExecute(JSONObject jsonO,
			HttpServletRequest request, HttpServletResponse response,
			String uuid) {
		long startTime = new Date().getTime();
		ResponseBaseBean baseBean = new ResponseBaseBean();
		baseBean.setTransCode(TRANSCODE);
		baseBean.initStatus(ResponseStatusEnum.RESPONSE_SUCCESS);
		
		String userId = String.valueOf(jsonO.get("userId"));					//用户唯一标识
		String newBankCardNum = String.valueOf(jsonO.get("newBankCardNum"));	//新绑定银行卡号
		String telephone = String.valueOf(jsonO.get("telephone"));				//手机号
		String captcha = String.valueOf(jsonO.get("captcha"));					//验证码
		String smsType = String.valueOf(jsonO.get("smsType"));					//验证码类型
				
		if(StringsUtil.isNull(userId) || StringsUtil.isNull(newBankCardNum)
				|| StringsUtil.isNull(telephone) || StringsUtil.isNull(captcha) || StringsUtil.isNull(smsType)){
			
			baseBean.initStatus(ResponseStatusEnum.RESPONSE_BICHUAN_PARAMES_EMPTOY);
			return baseBean;
		}
		
		try{
			// 校验验证码
			boolean checkFlag = true;
			// 校验具体实现代码,涉及到短信发送表(发送时间,验证码,状态-有/无效,超时时间-秒)
			if (!checkFlag) {
				baseBean.initStatus(ResponseStatusEnum.RESPONSE_SMS_IS_ERROR);
				return baseBean;
			}
			SysUser user = userRepository.findById(Integer.valueOf(userId));
			 if(null == user) {
				baseBean.initStatus(ResponseStatusEnum.RESPONSE_USER_ISNOT_FIND);
				return baseBean;
			}
			 //申请信息元素表-获取必须字段信息
			 String eNO = "";
			 String bindCardNO = "";
			 String bindPhone = "";
			 String oldbankName = "";
					
			//根据新卡前几位判断是否可支持银行-卡宾表
			
			
			//开始换卡
			baseBean = changCard(baseBean, jsonO, user, eNO, bindCardNO, bindPhone, oldbankName);
		
		}catch(Exception e){
			long elapsedTime = new Date().getTime() - startTime;
			baseBean.initStatus(ResponseStatusEnum.RESPONSE_FAILED);
			sysErrorLogSystemService.createExtErrorLog(userId, TRANSCODE, jsonO.toString(), e.getMessage(), null, baseBean, uuid, elapsedTime);
			
		}
		return baseBean;
	}

	/**
	 * 调换卡接口
	 * @param baseBean
	 * @param jsonO
	 * @param user
	 * @param eNO
	 * @param bindCardNO
	 * @param bindPhone
	 * @param oldbankName
	 * @return
	 */
	public ResponseBaseBean changCard(ResponseBaseBean baseBean, JSONObject jsonO, SysUser user, String eNO, String bindCardNO, String bindPhone,
			String oldbankName){
		
		String newBankCardNum = String.valueOf(jsonO.get("newBankCardNum"));	//新绑定银行卡号
		String newBankName = String.valueOf(jsonO.get("newBankName"));			//新绑定卡银行名称
		String telephone = String.valueOf(jsonO.get("telephone"));				//手机号
		String modiType = String.valueOf(jsonO.get("modiType"));				//11:换卡  22:修改绑定卡手机号
		
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			SHResponse object = null;
			map.put("trxCode", "HU0001");
			map.put("transCode", TRANSCODE);
			map.put("user", user);
			map.put("hubacctno", eNO);
			map.put("humerserno", "SYS" + System.currentTimeMillis());
			map.put("hucustname", user.getCusterName());
			map.put("huidno", newBankName);
			map.put("hubindcardno", bindCardNO);
			map.put("hunewcardno", newBankCardNum);
			map.put("hureservedphone", bindPhone);
			map.put("hunewreservedphone", telephone);
			map.put("humoditype", modiType);

			object = requestUtil.getCPI0001(map);
			if (null == object || object.getShHeadResponse() == null) {
				baseBean.initStatus(ResponseStatusEnum.RESPONSE_EXTERNAL_ERROR);
				return baseBean;
			}

			if ("S".equals(object.getShHeadResponse().gethutranstate())) {
				baseBean.setResponseBody(object.getShBodyResopnse());

				//换卡成功,更新数据表
				
			
			} else {
				//换卡失败,保存失败记录
				sysErrorLogSystemService.createExtErrorLog(String.valueOf(user.getId()), TRANSCODE, jsonO.toString(), String.valueOf(object), "", baseBean, "", 0L);
				baseBean.initStatus(ResponseStatusEnum.RESPONSE_FAILED);
				return baseBean;
			}
		} catch (Exception e) {
			logger.error("---换卡发生异常:", e);
			baseBean.initStatus(ResponseStatusEnum.RESPONSE_FAILED);
			sysErrorLogSystemService.createExtErrorLog(String.valueOf(user.getId()), TRANSCODE, jsonO.toString(), e.getMessage(), "", baseBean, "", 0L);
		}

		return baseBean;
	}
	
}