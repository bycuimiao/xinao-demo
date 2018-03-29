package com.xinao.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by guanshang on 2016/11/1 0001.
 */
public class User implements Serializable {
  private static final long serialVersionUID = -2035790832896240335L;
  /**
   * 用户ID.
   */
  private Long id;
  /**
   * 用户名称.
   */
  private String name;
  /**
   * 用户电话.
   */
  private String phone;
  /**
   * 创建时间.
   */
  private Date createTime;
  /**
   * 角色id.
   */
  private Long roleId;
  /**
   * 用户密码.
   */
  private String password;
  /**
   * 更新时间.
   */
  private Date updateTime;

  private Role role;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }
}
