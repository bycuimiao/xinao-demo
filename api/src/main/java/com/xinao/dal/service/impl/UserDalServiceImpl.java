/*
 * Created by cuimiao on 2018/3/28.
 */

package com.xinao.dal.service.impl;

import com.xinao.common.util.Parameters;
import com.xinao.dal.mapper.UserMapper;
import com.xinao.dal.service.UserDalService;
import com.xinao.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author cuimiao
 * @version 0.0.1
 * @Description:
 * @since 0.0.1 2018-03-28
 */
@Repository(value = "userDalService")
public class UserDalServiceImpl implements UserDalService{

  @Autowired
  private UserMapper userMapper;

  @Override
  public List<User> findUsers(Parameters parameters) {
    return userMapper.findUsers(parameters.getParameterMap());
  }

  @Override
  public Optional<List<User>> findUsersOptional(Parameters parameters) {

    List<User> users = userMapper.findUsers(parameters.getParameterMap());
    Optional<List<User>> optional = Optional.of(users);
    return optional;
  }

  /**
   * 查询参数列表.
   *
   * @return 查询参数
   */
  public static Parameters getParameters() {
    Parameters params = new Parameters();
    params.add("id")
        .add("name")//
        .add("phone")//
        .add("password")//
        .add("createTime")//
        .add("updateTime")//
        .add("roleId");
    return params;
  }


}