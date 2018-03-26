/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.session.impl;

import com.xinao.common.Result;
import com.xinao.common.State;
import com.xinao.entity.User;
import com.xinao.shiro.bo.UserOnlineBo;
import com.xinao.shiro.common.ShiroConstant;
import com.xinao.shiro.enums.SessionStatus;
import com.xinao.shiro.session.CustomShiroSessionDao;
import com.xinao.shiro.session.IShiroSessionRepository;
import com.xinao.shiro.util.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class CustomSessionManager {
  protected static Logger logger = LoggerFactory.getLogger(CustomSessionManager.class);

  @Autowired
  private IShiroSessionRepository shiroSessionRepository;

  @Autowired
  private CustomShiroSessionDao customShiroSessionDao;

  /**
   * 获取所有的有效Session用户.
   *
   * @return 在线用户列表
   */
  public List<UserOnlineBo> getAllUser() {
    List<UserOnlineBo> list = new ArrayList<UserOnlineBo>();
    //获取所有session
    Collection<Session> sessions = customShiroSessionDao.getActiveSessions();
    if (sessions != null && sessions.size() > 0) {
      for (Session session : sessions) {
        UserOnlineBo bo = getSessionBo(session);
        if (null != bo) {
          list.add(bo);
        }
      }
    }
    return list;
  }

  /**
   * 根据ID查询 SimplePrincipalCollection.
   *
   * @param userIds 用户ID
   * @return 用户SimplePrincipalCollection列表
   */
  public List<SimplePrincipalCollection> getSimplePrincipalCollectionByUserId(Long... userIds) {
    //把userIds 转成Set
    Set<Long> idset = StringUtils.array2Set(userIds);
    //获取所有session
    Collection<Session> sessions = customShiroSessionDao.getActiveSessions();
    //定义返回
    List<SimplePrincipalCollection> list = new ArrayList<SimplePrincipalCollection>();
    for (Session session : sessions) {
      //获取SimplePrincipalCollection
      Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
      if (null != obj && obj instanceof SimplePrincipalCollection) {
        //强转
        SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
        //判断用户，匹配用户ID。
        obj = spc.getPrimaryPrincipal();
        if (null != obj && obj instanceof User) {
          User user = (User) obj;
          //比较用户ID，符合即加入集合
          if (null != user && idset.contains(user.getId())) {
            list.add(spc);
          }
        }
      }
    }
    return list;
  }


  /**
   * 获取单个Session.
   *
   * @param sessionId sessionId
   * @return 在线用户信息
   */
  public UserOnlineBo getSession(String sessionId) {
    Session session = shiroSessionRepository.getSession(sessionId);
    UserOnlineBo bo = getSessionBo(session);
    return bo;
  }

  private UserOnlineBo getSessionBo(Session session) {
    //获取session登录信息。
    Object obj = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
    if (null == obj) {
      return null;
    }
    //确保是 SimplePrincipalCollection对象。
    if (obj instanceof SimplePrincipalCollection) {
      SimplePrincipalCollection spc = (SimplePrincipalCollection) obj;
      /**
       * 获取用户登录的，@link SampleRealm.doGetAuthenticationInfo(...)方法中
       * return new SimpleAuthenticationInfo(user,user.getPswd(), getName());的user 对象。
       */
      User user = null;
      Object pp = spc.getPrimaryPrincipal();
      if (pp != null) {
        if (pp instanceof SimplePrincipalCollection) {
          Object realpp = ((SimplePrincipalCollection) pp).getPrimaryPrincipal();
          if (realpp instanceof User) {
            user = (User) realpp;
          }
        } else if (pp instanceof User) {
          user = (User) pp;
        }
      }

      if (user != null) {
        //存储session + user 综合信息
        UserOnlineBo userBo = new UserOnlineBo();
        userBo.setUser(user);
        //最后一次和系统交互的时间
        userBo.setLastAccess(session.getLastAccessTime());
        //主机的ip地址
        userBo.setHost(session.getHost());
        //session ID
        userBo.setSessionId(session.getId().toString());
        //session最后一次与系统交互的时间
        userBo.setLastLoginTime(session.getLastAccessTime());
        //回话到期 ttl(ms)
        userBo.setTimeout(session.getTimeout());
        //session创建时间
        userBo.setStartTime(session.getStartTimestamp());
        //是否踢出
        SessionStatus sessionStatus = (SessionStatus) session.getAttribute(ShiroConstant.SESSION_STATUS);
        userBo.setSessionStatus(sessionStatus != null ? sessionStatus : SessionStatus.UNKNOW);
        return userBo;
      }
    }
    return null;
  }

  /**
   * 改变Session状态.
   *
   * @param sessionStatus sessionStatus
   * @param sessionIds    sessionIds
   * @return 是否成功
   */
  public Result<SessionStatus, State> changeSessionStatus(SessionStatus sessionStatus, String sessionIds) {
    Result<SessionStatus, State> rst = new Result<>();
    try {
      String[] sessionIdArray = null;
      if (sessionIds.indexOf(",") == -1) {
        sessionIdArray = new String[]{sessionIds};
      } else {
        sessionIdArray = sessionIds.split(",");
      }
      for (String id : sessionIdArray) {
        Session session = shiroSessionRepository.getSession(id);
        session.setAttribute(ShiroConstant.SESSION_STATUS, sessionStatus);
        customShiroSessionDao.update(session);
      }

      rst.setCode(State.SUCCESS);
      rst.setData(sessionStatus);
    } catch (Exception e) {
      logger.error("changeSessionStatus error:sessionIds:" + sessionIds, e);
      rst.setCode(State.FAILED);
      rst.setMessage(e.getMessage());
      rst.setData(sessionStatus);
    }
    return rst;
  }

  /**
   * 查询要禁用的用户是否在线.
   *
   * @param id     用户ID
   * @param status 用户状态
   */
  public void forbidUserById(Long id, SessionStatus status) {
    //获取所有在线用户
    for (UserOnlineBo bo : getAllUser()) {
      Long userId = bo.getUser().getId();
      //匹配用户ID
      if (userId.equals(id)) {
        //获取用户Session
        Session session = shiroSessionRepository.getSession(bo.getSessionId());
        //标记用户Session
        SessionStatus sessionStatus = (SessionStatus) session.getAttribute(ShiroConstant.SESSION_STATUS);
        if (sessionStatus.getCode() != status.getCode()) {
          session.setAttribute(ShiroConstant.SESSION_STATUS, status);
          //更新Session
          customShiroSessionDao.update(session);
        }
      }
    }
  }

  /**
   * 查询要禁用的用户是否在线.
   *
   * @param ids    用户ID
   * @param status 用户状态
   */
  public void forbidUserByIds(List<Long> ids, SessionStatus status) {
    if (ids == null || ids.size() == 0) {
      return;
    }

    //获取所有在线用户
    for (UserOnlineBo bo : getAllUser()) {
      Long userId = bo.getUser().getId();
      //匹配用户ID
      if (ids.contains(userId)) {
        //获取用户Session
        Session session = shiroSessionRepository.getSession(bo.getSessionId());
        //标记用户Session
        SessionStatus sessionStatus = (SessionStatus) session.getAttribute(ShiroConstant.SESSION_STATUS);
        if (sessionStatus.getCode() != status.getCode()) {
          session.setAttribute(ShiroConstant.SESSION_STATUS, status);
          //更新Session
          customShiroSessionDao.update(session);
        }
      }
    }
  }


  public void setShiroSessionRepository(IShiroSessionRepository shiroSessionRepository) {
    this.shiroSessionRepository = shiroSessionRepository;
  }

  public void setCustomShiroSessionDao(CustomShiroSessionDao customShiroSessionDao) {
    this.customShiroSessionDao = customShiroSessionDao;
  }

}
