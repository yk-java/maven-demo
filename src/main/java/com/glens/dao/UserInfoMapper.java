package com.glens.dao;

import com.glens.model.UserInfo;

import java.util.List;

public interface UserInfoMapper {
    List<UserInfo> findAll();
    int deleteByPrimaryKey(Integer userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}