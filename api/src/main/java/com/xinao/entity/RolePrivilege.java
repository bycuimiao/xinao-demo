/*
 * Created by guanshang on 2016-11-03.
 */

package com.xinao.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-03
 */
public class RolePrivilege implements Serializable {
  private static final long serialVersionUID = 9203299834369656093L;

  private Long id;
  private Long privilegeId;
  private Long roleId;
  private Date createTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPrivilegeId() {
    return privilegeId;
  }

  public void setPrivilegeId(Long privilegeId) {
    this.privilegeId = privilegeId;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
}
