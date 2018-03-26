/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.xinao.shiro.common.ShiroConstant;
import com.xinao.shiro.enums.SessionStatus;
import com.xinao.shiro.token.manager.TokenManager;
import com.xinao.shiro.util.LoggerUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class SimpleAuthFilter extends AccessControlFilter {
  private Logger logger = LoggerFactory.getLogger(SimpleAuthFilter.class);

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    try {
      HttpServletRequest req = (HttpServletRequest) request;
      logger.info("request url={},cookies info:{},detail info:{}",//
          new Object[]{req.getRequestURI(),//
              JSON.toJSONString(req.getCookies()),//
              JSON.toJSONString(req.getRemoteUser()) + ";" + getIpAddr(req) + ":" + req.getRemotePort() + ";" + req.getHeader("User-Agent")});
    } catch (Exception e) {
      logger.error("print request info error:", e);
    }

    HttpServletRequest httpRequest = ((HttpServletRequest) request);
    String url = httpRequest.getRequestURI();
    if (url.startsWith("/open/")) {
      return Boolean.TRUE;
    }
    Subject subject = getSubject(request, response);
    //未登录直接跳过该拦截器，登录拦截器会验证登录
    if (subject.getPrincipal() == null) {
      return Boolean.TRUE;
    }

    Session session = subject.getSession();
    SessionStatus sessionStatus = (SessionStatus) session.getAttribute(ShiroConstant.SESSION_STATUS);
    if (SessionStatus.KITOUT.getCode() == sessionStatus.getCode()) {
      LoggerUtils.warn(this.getClass(), "当前用户被管理员踢出");
      this.doLogout(subject);
      // 判断是不是Ajax请求
      if (ShiroFilterUtils.isAjax(request)) {
        ShiroFilterUtils.errorRstOut("您已被管理员踢出,请联系管理员", sessionStatus, request, response);
        return Boolean.FALSE;
      }
      try {
        if (ShiroFilterUtils.isAppRequest(request)) {
          ShiroFilterUtils.errorRstOut("您已被管理员踢出,请联系管理员", sessionStatus, request, response);
        } else {
          WebUtils.saveRequest(request);
          WebUtils.issueRedirect(request, response, ShiroFilterUtils.LOGIN_URL);
        }
      } catch (Exception e) {
        logger.warn("WebUtils.issueRedirect error:" + ShiroFilterUtils.LOGIN_URL, e.getMessage());
      }
      return Boolean.FALSE;
    } else if (SessionStatus.FORBITLOGIN.getCode() == sessionStatus.getCode()) {
      LoggerUtils.warn(this.getClass(), "当前用户被管理员禁止登录");
      this.doLogout(subject);
      // 判断是不是Ajax请求
      if (ShiroFilterUtils.isAjax(request)) {
        ShiroFilterUtils.errorRstOut("您已被管理员禁止登录,请联系管理员", sessionStatus, request, response);
        return Boolean.FALSE;
      }
      try {
        if (ShiroFilterUtils.isAppRequest(request)) {
          ShiroFilterUtils.errorRstOut("您已被管理员禁止登录,请联系管理员", sessionStatus, request, response);
        } else {
          WebUtils.saveRequest(request);
          WebUtils.issueRedirect(request, response, ShiroFilterUtils.LOGIN_URL);
        }
      } catch (Exception e) {
        logger.warn("WebUtils.issueRedirect error:" + ShiroFilterUtils.LOGIN_URL, e.getMessage());
      }
      return Boolean.FALSE;
    } else if (SessionStatus.REMOTELOGIN.getCode() == sessionStatus.getCode()) {
      LoggerUtils.warn(this.getClass(), "当前用户异地登录");
      this.doLogout(subject);
      // 判断是不是Ajax请求
      if (ShiroFilterUtils.isAjax(request)) {
        ShiroFilterUtils.errorRstOut("账号异地登录,请重新登录", sessionStatus, request, response);
        return Boolean.FALSE;
      }
      try {
        if (ShiroFilterUtils.isAppRequest(request)) {
          ShiroFilterUtils.errorRstOut("账号异地登录,请重新登录", sessionStatus, request, response);
        } else {
          WebUtils.saveRequest(request);
          WebUtils.issueRedirect(request, response, ShiroFilterUtils.LOGIN_URL);
        }
      } catch (Exception e) {
        logger.warn("WebUtils.issueRedirect error:" + ShiroFilterUtils.LOGIN_URL, e.getMessage());
      }
      return Boolean.FALSE;
    } else if (SessionStatus.RELOGIN.getCode() == sessionStatus.getCode()) {
      LoggerUtils.warn(this.getClass(), "账号信息变动，需要重新登录");
      this.doLogout(subject);
      // 判断是不是Ajax请求
      if (ShiroFilterUtils.isAjax(request)) {
        ShiroFilterUtils.errorRstOut("账号信息变动，需要重新登录", sessionStatus, request, response);
        return Boolean.FALSE;
      }
      try {
        if (ShiroFilterUtils.isAppRequest(request)) {
          ShiroFilterUtils.errorRstOut("账号信息变动，需要重新登录", sessionStatus, request, response);
        } else {
          WebUtils.saveRequest(request);
          WebUtils.issueRedirect(request, response, ShiroFilterUtils.LOGIN_URL);
        }
      } catch (Exception e) {
        logger.warn("WebUtils.issueRedirect error:" + ShiroFilterUtils.LOGIN_URL, e.getMessage());
      }
      return Boolean.FALSE;
    } else if (SessionStatus.OFFLINE.getCode() == sessionStatus.getCode()) {
      LoggerUtils.warn(this.getClass(), "账号已离线，需要重新登录");
      this.doLogout(subject);
      // 判断是不是Ajax请求
      if (ShiroFilterUtils.isAjax(request)) {
        ShiroFilterUtils.errorRstOut("账号已离线，需要重新登录", sessionStatus, request, response);
        return Boolean.FALSE;
      }
      try {
        if (ShiroFilterUtils.isAppRequest(request)) {
          ShiroFilterUtils.errorRstOut("账号已离线，需要重新登录", sessionStatus, request, response);
        } else {
          WebUtils.saveRequest(request);
          WebUtils.issueRedirect(request, response, ShiroFilterUtils.LOGIN_URL);
        }
      } catch (Exception e) {
        logger.warn("WebUtils.issueRedirect error:" + ShiroFilterUtils.LOGIN_URL, e.getMessage());
      }
      return Boolean.FALSE;
    }
    return Boolean.TRUE;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    return Boolean.FALSE;
  }

  /**
   * 退出登录，清空权限.
   *
   * @param subject subject
   */
  private void doLogout(Subject subject) {
    TokenManager.clearNowUserAuth();
    subject.logout();
  }

  /**
   * 获取真实Ip.
   *
   * @param request request请求
   * @return ip地址
   */
  private String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader(" x-forwarded-for ");
    if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
      ip = request.getHeader(" Proxy-Client-IP ");
    }
    if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
      ip = request.getHeader(" WL-Proxy-Client-IP ");
    }
    if (ip == null || ip.length() == 0 || " unknown ".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

}
