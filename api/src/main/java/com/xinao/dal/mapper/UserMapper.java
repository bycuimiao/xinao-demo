/*
 * Created by cuimiao on 2018/3/27.
 */

package com.xinao.dal.mapper;

import com.xinao.common.util.Parameters;
import com.xinao.entity.User;

import java.util.List;
import java.util.Map;

/**
 * @author cuimiao
 * @version 0.0.1
 * @since 0.0.1 2018-03-27
 */
public interface UserMapper {
  User findUser();
  List<User> findUsers(Map<String, Object> params);
}