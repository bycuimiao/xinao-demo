/*
 * Created by cuimiao on 2018/3/27.
 */

package com.xinao.bl.service.impl;

import com.xinao.dal.mapper.UserMapper;
import com.xinao.entity.User;
import com.xinao.bl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cuimiao
 * @version 0.0.1
 * @Description:
 * @since 0.0.1 2018-03-27
 */
@Service
public class UserServiceImpl implements UserService {

  @Autowired(required = false)
  private UserMapper userMapper;

  @Override
  public User findUser() {
    User user = new User();
    user.setName("cuimiao");
    user.setPhone("cuimiao");
    user.setPassword("123456");
    userMapper.findUser();
    return user;
  }
}