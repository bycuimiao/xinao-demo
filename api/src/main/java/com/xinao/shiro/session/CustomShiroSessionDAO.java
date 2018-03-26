/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.session;

import com.xinao.shiro.util.LoggerUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class CustomShiroSessionDao extends AbstractSessionDAO {

  private IShiroSessionRepository shiroSessionRepository;


  public IShiroSessionRepository getShiroSessionRepository() {
    return shiroSessionRepository;
  }

  public void setShiroSessionRepository(
      IShiroSessionRepository shiroSessionRepository) {
    this.shiroSessionRepository = shiroSessionRepository;
  }

  @Override
  public void update(Session session) throws UnknownSessionException {
    getShiroSessionRepository().saveSession(session);
  }

  @Override
  public void delete(Session session) {
    if (session == null) {
      LoggerUtils.error(this.getClass(), "session is not null");
      return;
    }
    Serializable id = session.getId();
    if (id != null) {
      getShiroSessionRepository().deleteSession(id);
    }
  }

  @Override
  public Collection<Session> getActiveSessions() {
    return getShiroSessionRepository().getAllSessions();
  }

  @Override
  protected Serializable doCreate(Session session) {
    Serializable sessionId = this.generateSessionId(session);
    this.assignSessionId(session, sessionId);
    getShiroSessionRepository().saveSession(session);
    return sessionId;
  }

  @Override
  protected Session doReadSession(Serializable sessionId) {
    return getShiroSessionRepository().getSession(sessionId);
  }
}
