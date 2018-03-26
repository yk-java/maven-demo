package com.glens.service;

import com.glens.model.UserInfo;

import java.util.List;


public interface UserInfoService {

	List<UserInfo> findAll();
	
	int deleteByPrimaryKey(Integer userId);

	int insert(UserInfo record);

	int insertSelective(UserInfo record);

	UserInfo selectByPrimaryKey(Integer userId);

	int updateByPrimaryKeySelective(UserInfo record);

	int updateByPrimaryKey(UserInfo record);
}
