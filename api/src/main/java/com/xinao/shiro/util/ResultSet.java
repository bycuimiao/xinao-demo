/*
 * Created by guanshang on 2016-11-10.
 */

package com.xinao.shiro.util;


import com.xinao.common.Result;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-10
 */
public class ResultSet<D, C> extends Result<List<D>, C> implements Serializable {
  private static final long serialVersionUID = -8555143484177007562L;

  private String url;

  private Map<String, Object> userInfo;

  private int total = 0;

  public String getUrl() {
    return url;
  }

  public Map<String, Object> getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(Map<String, Object> userInfo) {
    this.userInfo = userInfo;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

}