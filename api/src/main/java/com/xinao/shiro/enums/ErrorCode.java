/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.enums;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public enum ErrorCode {
  UNKNOW(-1, "unknow"), //
  NOTPRIVILEGE(0, "无权限"), //无权限
  NOTFOUND(1, "404"), //404
  NOTLOGIN(2, "无登录");

  private int code;
  private String name;

  ErrorCode(int code, String name) {
    this.code = code;
    this.name = name;
  }

  /**
   * 获取值.
   *
   * @param code code
   * @return SessionStatus
   */
  public static ErrorCode valueOf(Integer code) {
    if (code == null) {
      return UNKNOW;
    }
    for (ErrorCode aenum : values()) {
      if (aenum.code == code) {
        return aenum;
      }
    }
    return UNKNOW;
  }

  /**
   * 获取值.
   *
   * @param name name
   * @return SessionStatus
   */
  public static ErrorCode valueOf2(String name) {
    try {
      return valueOf(name);
    } catch (Exception e) {
      return UNKNOW;
    }
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}