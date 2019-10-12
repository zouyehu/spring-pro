package com.zyh.hu.repository.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.zyh.hu.repository.AppProperRepository;
import com.zyh.hu.service.BaseQueryService;

@SuppressWarnings("rawtypes")
@Component
public class AppProperRepositoryImpl extends BaseQueryService implements AppProperRepository{

	@SuppressWarnings("unchecked")
	@Override
	public String getValueByKey(String key, String appId) {
		String sql = "SELECT a.APP_ID,a.KEY_,a.VALUE_,a.DESCRIPTION_ from t_app_properties a where a.KEY_=:key and a.APP_ID=:appId";
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("key", key);
		conditionMap.put("appId", appId);
		Object[] retval = (Object[]) this.findOneResultObjectBySql(sql, conditionMap);
		if (null != retval && retval.length >=4){
			return (String) retval[2];
		}
		return null;
	}

}
