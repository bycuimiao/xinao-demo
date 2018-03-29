/*
 * Created by cuimiao on 2018/3/27.
 */

package com.xinao.bl.service.impl;

import com.xinao.common.Result;
import com.xinao.common.State;
import com.xinao.common.util.Parameters;
import com.xinao.dal.mapper.UserMapper;
import com.xinao.dal.service.UserDalService;
import com.xinao.dal.service.impl.UserDalServiceImpl;
import com.xinao.entity.User;
import com.xinao.bl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.UnrecoverableKeyException;
import java.util.List;
import java.util.Optional;

/**
 * @author cuimiao
 * @version 0.0.1
 * @Description:
 * @since 0.0.1 2018-03-27
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

  @Autowired(required = false)
  private UserMapper userMapper;

  @Autowired
  private UserDalService userDalService;

  @Override
  public User findUser() {
    User user = new User();
    user.setName("cuimiao");
    user.setPhone("cuimiao");
    user.setPassword("123456");
    userMapper.findUser();
    return user;
  }

  @Override
  public Result<User, State> findUserByPhone(String phone) {
    Result<User, State> result = new Result<>();
    try {
      Parameters parameters = UserDalServiceImpl.getParameters();
      parameters.eq("phone", phone);
      //List<User> userList = userDalService.findUsers(parameters);
      Optional<List<User>> optional = userDalService.findUsersOptional(parameters);
      List<User> users = optional.get();
      if (users.isEmpty() || users.size() > 1) {
        result.setCode(State.FAILED);
        return result;
      }
      result.setCode(State.SUCCESS);
      result.setData(users.get(0));
    } catch (Exception e) {
      //TODO
      System.out.println(e.getMessage());
      result.setCode(State.FAILED);
    }
    return result;
  }

  @Override
  public Result<User, State> login(String phone, String password) {
    Result<User, State> result = new Result<>();
    try {
      Parameters parameters = UserDalServiceImpl.getParameters();
      parameters.eq("phone", phone);
      parameters.eq("password", password);
      //List<User> userList = userDalService.findUsers(parameters);
      Optional<List<User>> optional = userDalService.findUsersOptional(parameters);
      List<User> users = optional.get();
      if (users.isEmpty() || users.size() > 1) {
        result.setCode(State.FAILED);
        return result;
      }
      result.setCode(State.SUCCESS);
      result.setData(users.get(0));
    } catch (Exception e) {
      //TODO
      result.setCode(State.FAILED);
    }
    return result;
  }
}