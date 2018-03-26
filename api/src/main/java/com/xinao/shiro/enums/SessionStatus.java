/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.enums;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public enum SessionStatus {
  UNKNOW(-1, "unknow"), //
  ONLINE(0, "online"), //在线
  OFFLINE(1, "offline"), //离线
  FORBITLOGIN(2, "forbitlogin"),//限制登录
  REMOTELOGIN(3, "remotelogin"),//异地登录
  RELOGIN(4, "relogin"),//重新登录
  KITOUT(5, "kitout");//踢出

  private int code;
  private String name;

  SessionStatus(int code, String name) {
    this.code = code;
    this.name = name;
  }

  /**
   * 获取值.
   *
   * @param code code
   * @return SessionStatus
   */
  public static SessionStatus valueOf(Integer code) {
    if (code == null) {
      return UNKNOW;
    }
    for (SessionStatus aenum : values()) {
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
  public static SessionStatus valueOf2(String name) {
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