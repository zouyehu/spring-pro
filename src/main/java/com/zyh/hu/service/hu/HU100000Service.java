package com.zyh.hu.service.hu;

import java.util.Date;

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
import com.zyh.hu.utils.AccessTokenEncryptUtil;
import com.zyh.hu.utils.StringsUtil;

/**
 * 简单接口调度测试样例
 * @author HU
 *
 */
@Service("HU100000Service")
public class HU100000Service implements BaseService{
	
	private static final Logger logger = LoggerFactory.getLogger(HU100000Service.class);
	private static final String transCode = "HU100000";

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AccessTokenEncryptUtil accessTokenEncryptUtil;
	
	public ResponseBaseBean doExecute(JSONObject jsonO, HttpServletRequest request, HttpServletResponse response,
			String uuid) {
		ResponseBaseBean baseBean = new ResponseBaseBean();
		baseBean.setTransCode(transCode);
		baseBean.initStatus(ResponseStatusEnum.RESPONSE_SUCCESS);
		
		String userName = String.valueOf(jsonO.get("userName"));		//用户姓名
		String password = String.valueOf(jsonO.get("password"));		//用户密码
		String address = String.valueOf(jsonO.get("address"));			//用户地址
		String mflag = String.valueOf(jsonO.get("mflag"));				//密码标识-1:修改
		
		if (StringsUtil.isNull(userName) || StringsUtil.isNull(password)) {
			baseBean.initStatus(ResponseStatusEnum.RESPONSE_BICHUAN_PARAMES_EMPTOY);
			return baseBean;
		}
		SysUser user = userRepository.findByLoginName(userName);
		if (null == user) {
			user = new SysUser();
			user.setLoginName(userName);
			user.setLoginPassword(password);
			user.setAddress(address);
			user.setCreateTime(new Date());
			String appToken = accessTokenEncryptUtil.getValidAccessToken(user);
			if (!StringsUtil.isNull(appToken)) {
				user.setAccessToken(appToken);
			}
			userRepository.save(user);
			
		}
		
		if ("1".equals(mflag) && !password.equals(user.getLoginPassword())) {
			userRepository.updateLoginPassword(userName, password, new Date());
			logger.info("---用户密码修改成功");
		}
		
		return baseBean;
	}

}
