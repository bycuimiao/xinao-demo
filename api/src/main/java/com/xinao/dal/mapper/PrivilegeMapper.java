/*
 * Created by cuimiao on 2018/3/29.
 */

package com.xinao.dal.mapper;

import com.xinao.entity.Privilege;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author cuimiao
 * @version 0.0.1
 * @since 0.0.1 2018-03-29
 */
public interface PrivilegeMapper {
  List<Privilege> findPrivileges(Long id);
}