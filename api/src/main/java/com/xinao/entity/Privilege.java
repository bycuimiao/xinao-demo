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
public class Privilege implements Serializable {
  private static final long serialVersionUID = -7695676135235240140L;
  /**
   * 权限ID.
   */
  private Long id;
  /**
   * 权限名称.
   */
  private String name;
  /**
   * 权限url.
   */
  private String url;

  private String type;

  /**
   * 创建时间.
   */
  private Date createTime;
  /**
   * 修改时间.
   */
  private Date updateTime;

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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
