/*
 * Created by cuimiao on 2018/3/27.
 */

package com.xinao.bl.service;

import com.xinao.common.Limit;
import com.xinao.common.Result;
import com.xinao.common.ResultSet;
import com.xinao.common.State;
import com.xinao.entity.User;

/**
 * @author cuimiao
 * @version 0.0.1
 * @since 0.0.1 2018-03-27
 */
public interface UserService {
  User findUser();
  Result<User,State> findUserByPhone(String phone);
  Result<User,State> login(String phone,String password);

  ResultSet<User,State> findUsers(Limit limit);
}