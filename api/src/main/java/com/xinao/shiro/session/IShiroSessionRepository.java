/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.session;

import org.apache.shiro.session.Session;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public interface IShiroSessionRepository {


  /**
   * 存储Session.
   *
   * @param session session
   */
  void saveSession(Session session);

  /**
   * 删除session.
   *
   * @param sessionId sessionId
   */
  void deleteSession(Serializable sessionId);

  /**
   * 获取session.
   *
   * @param sessionId sessionId
   * @return session信息
   */
  Session getSession(Serializable sessionId);

  /**
   * 获取所有sessoin.
   *
   * @return session列表
   */
  Collection<Session> getAllSessions();
}
