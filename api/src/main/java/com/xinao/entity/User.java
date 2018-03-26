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
   * 企业ID.
   */
  private Long enterpriseId;
  /**
   * 角色id.
   */
  private Long roleId;
  /**
   * 部门id.
   */
  private Long departmentId;
  /**
   * 用户密码.
   */
  private String password;
  /**
   * 邮箱.
   */
  private String email;
  /**
   * 地址.
   */
  private String address;
  /**
   * 更新时间.
   */
  private Date updateTime;

  /**
   * 上次登录时间.
   */
  private Date lastLoginTime;
  /**
   * 操作人ID.
   */
  private Long operateId;


  /**
   * 导出配置.
   */
  private String exportType;

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

  public Long getEnterpriseId() {
    return enterpriseId;
  }

  public void setEnterpriseId(Long enterpriseId) {
    this.enterpriseId = enterpriseId;
  }

  public Long getRoleId() {
    return roleId;
  }

  public void setRoleId(Long roleId) {
    this.roleId = roleId;
  }

  public Long getDepartmentId() {
    return departmentId;
  }

  public void setDepartmentId(Long departmentId) {
    this.departmentId = departmentId;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public Long getOperateId() {
    return operateId;
  }

  public void setOperateId(Long operateId) {
    this.operateId = operateId;
  }

  public String getExportType() {
    return exportType;
  }

  public void setExportType(String exportType) {
    this.exportType = exportType;
  }
}
