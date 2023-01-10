package com.zou.blog.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zou.blog.mapper.custom.UserMapperCustom;
import com.zou.blog.mapper.generator.UserMapper;
import com.zou.blog.model.domain.User;
import com.zou.blog.service.UserService;

@Service
@Transactional(rollbackFor = RuntimeException.class)
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapperCustom userMapperCustom;
	@Autowired
	private UserMapper userMapper;

	@Override
	public User getByNameAndPwd(String name, String pwd) throws Exception {
		return userMapperCustom.getByNameAndPwd(name, pwd);
	}

	@Override
	public void updateDatum(User user) throws Exception {
		userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public User findUser() {
		List<User> lists = userMapperCustom.findUser();
		if (lists != null && lists.size() > 0) {
			return lists.get(0);
		} else {
			return new User();
		}
	}

	@Override
	public User findByUserIdAndUserPwd(Integer userId, String formerlyPwd) throws Exception {
		return userMapperCustom.findByUserIdAndUserPwd(userId, formerlyPwd);
	}

	@Override
	public void updateLoginLastTime(Date date, Integer userId) throws Exception {
		User user = new User();
		user.setLoginLastTime(date);
		user.setUserId(userId);
		userMapper.updateByPrimaryKeySelective(user);

	}

	@Override
	public void updateUserNormal(Integer userId) throws Exception {
		User user = new User();
		user.setLoginError(0);
		user.setLoginEnable("false");
		user.setUserId(userId);
		user.setLoginLastTime(new Date());
		userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public Integer updateError() throws Exception {
		User user = this.findUser();
		user.setLoginError((user.getLoginError() == null ? 0 : user.getLoginError()) + 1);
		userMapper.updateByPrimaryKeySelective(user);
		return user.getLoginError();
	}

	@Override
	public void updateLoginEnable(String enable, Integer error) throws Exception {
		User user = this.findUser();
		user.setLoginEnable(enable);
		user.setLoginError(error);
		userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public void save(User user) {
		userMapper.insert(user);		
	}

}
