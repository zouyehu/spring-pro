package com.zyh.hu.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.zyh.hu.entity.SysUser;



public interface UserRepository extends BaseRepository<SysUser, Long>{

	/**
	 * 根据登录名得到 user对象
	 * @param loginName
	 * @return
	 */
	public SysUser findById(int id);
	/**
	 * 根据登录名得到 user对象
	 * @param loginName
	 * @return
	 */
	public SysUser findByLoginName(String loginName);
	
	/**
	 * 根据登录名名更新用户密码
	 * @param loginName
	 * @param loginPassword
	 * @param modifyPasswordTime
	 */
	@Transactional
	@Modifying
	@Query(value = "update SYS_USER_INFO s set s.LOGIN_PASSWORD=:loginPassword,set s.MODIFY_PASSWORD_TIME=:modifyPasswordTime where s.LOGIN_NAME=:loginName",nativeQuery = true)
	public void updateLoginPassword(@Param("loginName") String loginName, @Param("loginPassword") String loginPassword,
			@Param("modifyPasswordTime") Date modifyPasswordTime);
	
	/**
	 * 根据用户名查询user对象(存在重名,故用list接收)
	 * @param custerName
	 * @return
	 */
	@Query("select u from SysUser u where u.custerName=?1")
	public List<SysUser> findUsersByCusterName(String custerName);
	
}
