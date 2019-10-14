package com.zyh.hu.quartz.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.zyh.hu.repository.UserRepository;

public class TestfindUserNums {

	@Autowired
	private UserRepository userRepository;
	
	public long findUserNums(){
		long nums = userRepository.count();
		System.err.println("---目前总用户数是:" + nums);
		return nums;
	}
}
