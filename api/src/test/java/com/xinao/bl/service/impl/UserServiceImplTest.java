/*
 * Created by cuimiao on 2018/4/4.
 */

package com.xinao.bl.service.impl;

import com.xinao.base.BaseTest;
import com.xinao.bl.service.UserService;
import com.xinao.common.Limit;
import com.xinao.common.Result;
import com.xinao.common.ResultSet;
import com.xinao.common.State;
import com.xinao.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author cuimiao
 * @version 0.0.1
 * @since 0.0.1 2018-04-04
 */
public class UserServiceImplTest extends BaseTest{

  @Autowired
  private UserService userService;

  @Test
  public void findUsers() throws Exception {
    Limit limit = new Limit();
    limit.setLength(10);
    limit.setOffset(0);
    limit.setTotal(0);
    ResultSet<User,State> resultSet = userService.findUsers(limit);
    System.out.println(resultSet);
  }

}