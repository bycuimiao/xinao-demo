/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.bo;

import com.xinao.entity.User;
import com.xinao.shiro.enums.SessionStatus;

import java.io.Serializable;
import java.util.Date;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class UserOnlineBo implements Serializable {
  private static final long serialVersionUID = -2422840319977688733L;

  //Session Id
  private String sessionId;
  //Session Host
  private String host;
  //Session创建时间
  private Date startTime;
  //Session最后交互时间
  private Date lastAccess;
  //Session timeout
  private long timeout;
  //session 是否踢出
  private SessionStatus sessionStatus = SessionStatus.UNKNOW;

  private Date lastLoginTime;

  private User user;

  public UserOnlineBo() {
  }

  public Date getLastLoginTime() {
    return lastLoginTime;
  }

  public void setLastLoginTime(Date lastLoginTime) {
    this.lastLoginTime = lastLoginTime;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getLastAccess() {
    return lastAccess;
  }

  public void setLastAccess(Date lastAccess) {
    this.lastAccess = lastAccess;
  }

  public long getTimeout() {
    return timeout;
  }

  public void setTimeout(long timeout) {
    this.timeout = timeout;
  }

  public SessionStatus getSessionStatus() {
    return sessionStatus;
  }

  public void setSessionStatus(SessionStatus sessionStatus) {
    this.sessionStatus = sessionStatus;
  }
}
