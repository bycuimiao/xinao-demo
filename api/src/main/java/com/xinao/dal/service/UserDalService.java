/*
 * Created by cuimiao on 2018/3/28.
 */

package com.xinao.dal.service;

import com.xinao.common.util.Parameter;
import com.xinao.common.util.Parameters;
import com.xinao.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * @author cuimiao
 * @version 0.0.1
 * @Description:
 * @since 0.0.1 2018-03-28
 */
public interface UserDalService {
  List<User> findUsers(Parameters parameters);
  Optional<List<User>> findUsersOptional(Parameters parameters);
}