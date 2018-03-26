/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.filter;

import com.xinao.common.cache.ICache;
import com.xinao.shiro.common.ShiroConstant;
import com.xinao.shiro.enums.SessionStatus;
import com.xinao.shiro.session.IShiroSessionRepository;
import com.xinao.shiro.token.manager.TokenManager;
import com.xinao.shiro.util.LoggerUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.LinkedHashMap;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class KickoutSessionFilter extends AccessControlFilter {
  private Logger logger = LoggerFactory.getLogger(KickoutSessionFilter.class);

  //注入kickoutUrl
  public String kickoutUrl;
  //session获取
  public IShiroSessionRepository shiroSessionRepository;
  //redisCache
  public ICache redisCache;


  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    HttpServletRequest httpRequest = ((HttpServletRequest) request);
    String url = httpRequest.getRequestURI();
    Subject subject = getSubject(request, response);
    // 如果是相关目录 or 如果没有登录 就直接return true
    if (url.startsWith("/open/") || (!subject.isAuthenticated() && !subject.isRemembered())) {
      return Boolean.TRUE;
    }
    Session session = subject.getSession();
    Serializable sessionId = session.getId();
    /**
     * 判断是否已经踢出 1.如果是Ajax 访问，那么给予json返回值提示。 2.如果是普通请求，直接跳转到登录页.
     */
    SessionStatus sessionStatus = (SessionStatus) session.getAttribute(ShiroConstant.SESSION_STATUS);
    if (SessionStatus.KITOUT.getCode() == sessionStatus.getCode()) {
      LoggerUtils.warn(this.getClass(), "当前用户被管理员踢出");
      return Boolean.FALSE;
    }

    // 从缓存获取用户-Session信息 <UserId,SessionId>
    LinkedHashMap<Long, Serializable> infoMap = redisCache.get(ShiroConstant.ONLINE_USER, LinkedHashMap.class);
    // 如果不存在，创建一个新的
    infoMap = null == infoMap ? new LinkedHashMap<Long, Serializable>() : infoMap;

    // 获取tokenId
    Long userId = TokenManager.getUserId();

    // 更新存储到缓存1个小时（这个时间最好和session的有效期一致或者大于session的有效期）
    int expireTime = Long.valueOf(session.getTimeout() + 60 * 60).intValue();

    // 如果已经包含当前Session，并且是同一个用户，跳过。
    if (infoMap.containsKey(userId) && infoMap.containsValue(sessionId)) {
      redisCache.setex(ShiroConstant.ONLINE_USER, infoMap, expireTime);
      return Boolean.TRUE;
    }
    // 如果用户相同，Session不相同，那么就要处理了
    /**
     * 如果用户Id相同,Session不相同 1.获取到原来的session，并且标记为踢出。 2.继续走
     */
    if (infoMap.containsKey(userId) && !infoMap.containsValue(sessionId)) {
      Serializable oldSessionId = infoMap.get(userId);
      Session oldSession = shiroSessionRepository.getSession(oldSessionId);
      if (null != oldSession) {
        // 标记session异地登录
        oldSession.setAttribute(ShiroConstant.SESSION_STATUS, SessionStatus.REMOTELOGIN);
        shiroSessionRepository.saveSession(oldSession);// 更新session
        infoMap.remove(userId);
        LoggerUtils.fmtDebug(this.getClass(), "offline old session success,oldId[%s]", oldSessionId);
      } else {
        shiroSessionRepository.deleteSession(oldSessionId);
        infoMap.remove(userId);
      }

      infoMap.put(userId, sessionId);
      redisCache.setex(ShiroConstant.ONLINE_USER, infoMap, expireTime);

      return Boolean.TRUE;
    }

    if (!infoMap.containsKey(userId) && !infoMap.containsValue(sessionId)) {
      infoMap.put(userId, sessionId);
      redisCache.setex(ShiroConstant.ONLINE_USER, infoMap, expireTime);
      return Boolean.TRUE;
    }

    return Boolean.TRUE;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    // 先退出
    Subject subject = getSubject(request, response);
    //清空权限
    TokenManager.clearNowUserAuth();
    //退出登录
    subject.logout();
    // ajax请求
    if (ShiroFilterUtils.isAjax(request)) {
      ShiroFilterUtils.errorRstOut("current user is kick out by admin", null, request, response);
      return Boolean.FALSE;
    }
    try {
      if (ShiroFilterUtils.isAppRequest(request)) {
        ShiroFilterUtils.errorRstOut("current user is kick out by admin", null, request, response);
      } else {
        //重定向
        WebUtils.getSavedRequest(request);
        WebUtils.issueRedirect(request, response, getKickoutUrl());
      }
    } catch (Exception e) {
      logger.warn("WebUtils.issueRedirect error :" + getKickoutUrl(), e.getMessage());
    }
    return Boolean.FALSE;
  }


  public String getKickoutUrl() {
    return kickoutUrl;
  }

  public void setKickoutUrl(String kickoutUrl) {
    this.kickoutUrl = kickoutUrl;
  }

  public IShiroSessionRepository getShiroSessionRepository() {
    return shiroSessionRepository;
  }

  public void setShiroSessionRepository(IShiroSessionRepository shiroSessionRepository) {
    this.shiroSessionRepository = shiroSessionRepository;
  }

  public ICache getRedisCache() {
    return redisCache;
  }

  public void setRedisCache(ICache redisCache) {
    this.redisCache = redisCache;
  }
}

