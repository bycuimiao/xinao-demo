package com.xinao.shiro.session;

import com.xinao.common.util.LoggerUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.Collection;

/**
 * 开发公司：SOJSON在线工具 <p>
 * 版权所有：© www.sojson.com<p>
 * 博客地址：http://www.sojson.com/blog/  <p>
 * <p>
 * <p>
 * Session 操作
 * <p>
 * <p>
 * <p>
 * 区分　责任人　日期　　　　说明<br/>
 * 创建　周柏成　2016年6月2日 　<br/>
 *
 * @author zhou-baicheng
 * @version 1.0, 2016年6月2日 <br/>
 * @email so@sojson.com
 */
public class CustomShiroSessionDAO extends AbstractSessionDAO {

  private ShiroSessionRepository shiroSessionRepository;

  public ShiroSessionRepository getShiroSessionRepository() {
    return shiroSessionRepository;
  }

  public void setShiroSessionRepository(
      ShiroSessionRepository shiroSessionRepository) {
    this.shiroSessionRepository = shiroSessionRepository;
  }

  @Override
  public void update(Session session) throws UnknownSessionException {
    getShiroSessionRepository().saveSession(session);
    //TODO
  }

  @Override
  public void delete(Session session) {
    if (session == null) {
      LoggerUtils.error(getClass(), "Session 不能为null");
      return;
    }
    Serializable id = session.getId();
    if (id != null) {
      getShiroSessionRepository().deleteSession(id);
      //TODO
    }

  }

  @Override
  public Collection<Session> getActiveSessions() {
    //TODO
    return getShiroSessionRepository().getAllSessions();
    //return null;
  }

  @Override
  protected Serializable doCreate(Session session) {
    Serializable sessionId = this.generateSessionId(session);
    this.assignSessionId(session, sessionId);
    getShiroSessionRepository().saveSession(session);
    //TODO 报错session
    return sessionId;
  }

  @Override
  protected Session doReadSession(Serializable sessionId) {
    //TODO
    return getShiroSessionRepository().getSession(sessionId);
    //return null;
  }
}
