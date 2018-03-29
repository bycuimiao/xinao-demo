/*
 * Created by cuimiao on 2018/3/28.
 */

package com.xinao.dal.service.impl;

import com.xinao.common.util.Parameters;
import com.xinao.dal.mapper.PrivilegeMapper;
import com.xinao.dal.mapper.UserMapper;
import com.xinao.dal.service.PrivilegeDalService;
import com.xinao.dal.service.UserDalService;
import com.xinao.entity.Privilege;
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
@Repository(value = "privilegeDalService")
public class PrivilegeDalServiceImpl implements PrivilegeDalService {

  @Autowired
  private PrivilegeMapper privilegeMapper;


  @Override
  public List<Privilege> findPrivileges(Long userId) {
    return privilegeMapper.findPrivileges(userId);
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
        .add("url");
    return params;
  }


}