package com.glens.service.impl;

import com.glens.dao.UserInfoMapper;
import com.glens.model.UserInfo;
import com.glens.service.UserInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
@Service
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

	@Resource
	private UserInfoMapper mapper;

	@Override
	public List<UserInfo> findAll() {
		List<UserInfo> list = mapper.findAll();
		return list;
	}
	
	@Override
	public int deleteByPrimaryKey(Integer userId) {
		return mapper.deleteByPrimaryKey(userId);
	}

	@Override
	public int insert(UserInfo record) {
		return mapper.insert(record);
	}

	@Override
	public int insertSelective(UserInfo record) {
		return mapper.insertSelective(record);
	}

	@Override
	public UserInfo selectByPrimaryKey(Integer userId) {
		return mapper.selectByPrimaryKey(userId);
	}

	@Override
	public int updateByPrimaryKeySelective(UserInfo record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(UserInfo record) {
		return mapper.updateByPrimaryKey(record);
	}

}
