/*
 * Created by cuimiao on 2018/3/27.
 */

package com.xinao.bl.service;

import com.xinao.common.Result;
import com.xinao.common.State;
import com.xinao.entity.Privilege;
import com.xinao.entity.User;

import java.util.List;

/**
 * @author cuimiao
 * @version 0.0.1
 * @since 0.0.1 2018-03-27
 */
public interface PrivilegeService {
  Result<List<Privilege>,State> findPrivileges(Long userId);
}