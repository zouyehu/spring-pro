package com.zyh.hu.service.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyh.hu.entity.SysUser;
import com.zyh.hu.service.requestxml.SHBodyRequest;
import com.zyh.hu.service.requestxml.SHHeadRequest;
import com.zyh.hu.service.requestxml.SHRequest;
import com.zyh.hu.service.responsexml.SHResponse;
import com.zyh.hu.service.send.HTTPSend;
import com.zyh.hu.utils.DateUtil;

@Service
public class RequestUtil {

	@SuppressWarnings("rawtypes")
	@Autowired
	private HTTPSend hTTPSendImpl;

	//常量定义
	private final static String CHANNELID = "huGE";
	private final static String huREQSYS = "123";
	private final static String huTRANCODE = "hu_XUE_520";

	/**
	 * 封装请求报文通用接口
	 * 
	 * @param request
	 * @param transCode
	 * @param ckuser
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public SHResponse getSHXMLInfo(SHRequest request, String transCode,
			SysUser user) {
		SHResponse response = null;
		Map<String, String> saveMsg = new HashMap<String, String>();
		String loginName = user.getLoginName();
		saveMsg.put("loginName", loginName);
		saveMsg.put("transCode", transCode);
		response = (SHResponse) hTTPSendImpl.sendReqObj(request, SHResponse.class, saveMsg);
		return response;
	}

	/**
	 * 请求头封装
	 * 
	 * @param hutrancode
	 * @return
	 */
	public SHHeadRequest getSHHeadRequest(String hutrancode) {
		SHHeadRequest headRequest = new SHHeadRequest();
		Date currentDate = new Date();
		String transDate = DateUtil.format(currentDate, "yyyyMMdd");
		String transTime = DateUtil.format(currentDate, "HHmmss");
		String hutranserno = CHANNELID
				+ UUID.randomUUID().toString().replaceAll("-", "");

		headRequest.sethutrancode(huTRANCODE);
		headRequest.sethutranserno(hutranserno);
		headRequest.sethureqsys(huREQSYS);
		headRequest.sethureqdate(transDate);
		headRequest.sethureqtime(transTime);

		return headRequest;
	}

	/**
	 * 更换绑定银行卡
	 * 
	 * @param map
	 * @return
	 */
	public SHResponse getCPI0001(Map<String, Object> map) {
		SysUser user = (SysUser) map.get("userId");
		String trxCode = (String) map.get("trxCode");
		String transCode = (String) map.get("transCode");
		String hubacctno = (String) map.get("hubacctno"); 						// 子账号
		String humerserno = (String) map.get("humerserno"); 					// 流水号
		String hucustname = (String) map.get("hucustname"); 					// 姓名
		String huidno = (String) map.get("huidno"); 							// 身份证号
		String hubindcardno = (String) map.get("hubindcardno"); 				// 绑定卡号
		String hunewcardno = (String) map.get("hunewcardno"); 					// 新绑定卡号
		String huoldphone = (String) map.get("huoldphone"); 					// 手机号
		String hunewphone = (String) map.get("hunewphone"); 					// 新手机号
		String humoditype = (String) map.get("humoditype"); 					// 11:换卡	22:修改绑定卡手机号

		SHHeadRequest headRequest = this.getSHHeadRequest(trxCode);
		SHBodyRequest bodyRequest = new SHBodyRequest();
		bodyRequest.sethusubacctno(hubacctno);
		bodyRequest.sethumerserno(humerserno);
		bodyRequest.sethucustname(hucustname);
		bodyRequest.sethuidno(huidno);
		bodyRequest.sethubindcardno(hubindcardno);
		bodyRequest.sethunewcardno(hunewcardno);
		bodyRequest.sethureservedphone(huoldphone);
		bodyRequest.sethunewreservedphone(hunewphone);
		bodyRequest.sethumoditype(humoditype);
		SHRequest request = new SHRequest();
		request.setShHeadRequest(headRequest);
		request.setShBodyRequest(bodyRequest);

		SHResponse response = this.getSHXMLInfo(request, transCode, user);
		return response;
	}

}