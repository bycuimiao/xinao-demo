/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.session.impl;

import com.xinao.shiro.cache.JedisManager;
import com.xinao.shiro.common.ShiroConstant;
import com.xinao.shiro.enums.SessionStatus;
import com.xinao.shiro.session.IShiroSessionRepository;
import com.xinao.shiro.util.LoggerUtils;
import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class JedisShiroSessionRepository implements IShiroSessionRepository {

  private JedisManager jedisManager;

  @Override
  public void saveSession(Session session) {
    Long startTime = System.currentTimeMillis();
    if (session == null || session.getId() == null) {
      throw new NullPointerException("session is empty");
    }
    try {
      String key = buildRedisSessionKey(session.getId());

      // 不存在则添加。
      if (null == session.getAttribute(ShiroConstant.SESSION_STATUS)) {
        session.setAttribute(ShiroConstant.SESSION_STATUS, SessionStatus.ONLINE);
      }

      long sessionTimeOut = session.getTimeout() / 1000;
      Long expireTime = sessionTimeOut + ShiroConstant.SESSION_VAL_TIME_SPAN + (5 * 60);
      getJedisManager().saveValueByKey(key, session, expireTime.intValue());
    } catch (Exception e) {
      LoggerUtils.error(this.getClass(), "save session error id=" + session.getId(), e);
    }
    LoggerUtils.info(JedisShiroSessionRepository.class,"Search save Session cost:" + (System.currentTimeMillis() - startTime));
  }

  @Override
  public void deleteSession(Serializable id) {
    Long startTime = System.currentTimeMillis();
    if (id == null) {
      throw new NullPointerException("session id is empty");
    }
    try {
      getJedisManager().delByKey(buildRedisSessionKey(id));
    } catch (Exception e) {
      LoggerUtils.error(this.getClass(), "delete session error id=" + id, e);
    }
    LoggerUtils.info(JedisShiroSessionRepository.class,"Search del Session cost:" + (System.currentTimeMillis() - startTime));
  }

  @Override
  public Session getSession(Serializable id) {
    Long startTime = System.currentTimeMillis();
    if (id == null) {
      throw new NullPointerException("session id is empty");
    }
    Session session = null;
    try {
      session = getJedisManager().get(buildRedisSessionKey(id), Session.class);
    } catch (Exception e) {
      LoggerUtils.error(this.getClass(), "get session error id=" + id, e);
    }
    LoggerUtils.info(JedisShiroSessionRepository.class,"Search get Session cost:" + (System.currentTimeMillis() - startTime));
    return session;
  }

  @Override
  public Collection<Session> getAllSessions() {
    Set<Session> sessions = null;
    Set<String> hkeys = getJedisManager().getPatternKeys(ShiroConstant.REDIS_SHIRO_ALL);
    if (hkeys != null && hkeys.size() > 0) {
      sessions = new HashSet<Session>();
      Session session = null;
      for (String bs : hkeys) {
        session = getJedisManager().get(bs, Session.class);
        if (session instanceof Session) {
          sessions.add(session);
        }
      }
    }
    return sessions;
  }


  private String buildRedisSessionKey(Serializable sessionId) {
    return ShiroConstant.REDIS_SHIRO_SESSION + sessionId;
  }

  public JedisManager getJedisManager() {
    return jedisManager;
  }

  public void setJedisManager(JedisManager jedisManager) {
    this.jedisManager = jedisManager;
  }
}
