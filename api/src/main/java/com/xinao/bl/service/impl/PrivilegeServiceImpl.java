/*
 * Created by cuimiao on 2018/3/27.
 */

package com.xinao.bl.service.impl;

import com.xinao.api.LoginApi;
import com.xinao.bl.service.PrivilegeService;
import com.xinao.bl.service.UserService;
import com.xinao.common.Result;
import com.xinao.common.State;
import com.xinao.common.util.Parameters;
import com.xinao.dal.mapper.UserMapper;
import com.xinao.dal.service.PrivilegeDalService;
import com.xinao.dal.service.UserDalService;
import com.xinao.dal.service.impl.UserDalServiceImpl;
import com.xinao.entity.Privilege;
import com.xinao.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author cuimiao
 * @version 0.0.1
 * @Description:
 * @since 0.0.1 2018-03-27
 */
@Service(value = "privilegeService")
public class PrivilegeServiceImpl implements PrivilegeService {

  private Logger logger = LoggerFactory.getLogger(PrivilegeServiceImpl.class);

  @Autowired
  private PrivilegeDalService privilegeDalService;


  @Override
  public Result<List<Privilege>,State> findPrivileges(Long userId) {
    Result<List<Privilege>,State> result = new Result<>();
    try {
      List<Privilege> privileges = privilegeDalService.findPrivileges(userId);
      result.setCode(State.SUCCESS);
      result.setData(privileges);
    }catch (Exception e){
      logger.error(e.getMessage());
      result.setCode(State.FAILED);
    }
    return result;
  }
}