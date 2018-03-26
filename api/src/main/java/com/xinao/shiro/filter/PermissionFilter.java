/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.filter;

import com.xinao.shiro.common.ShiroConstant;
import com.xinao.shiro.enums.ErrorCode;
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
public class PermissionFilter extends AccessControlFilter {
  private Logger logger = LoggerFactory.getLogger(PermissionFilter.class);

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

    Subject subject = getSubject(request, response);
    // 先判断带参数的权限判断
    if (null != mappedValue) {
      String[] arra = (String[]) mappedValue;
      for (String permission : arra) {
        if (subject.isPermitted(permission)) {
          return Boolean.TRUE;
        }
      }
    }
    HttpServletRequest httpRequest = ((HttpServletRequest) request);
    String method = httpRequest.getMethod().toLowerCase();
    String uri = httpRequest.getRequestURI().toLowerCase();// 获取URI
    String basePath = httpRequest.getContextPath();// 获取basePath
    if (null != uri && uri.startsWith(basePath)) {
      uri = uri.replace(basePath, "");
    }

    if (subject.isPermitted(uri + ShiroConstant.SPLIT + method)) {
      return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    Subject subject = getSubject(request, response);
    //表示没有登录
    if (subject.getPrincipal() == null) {
      if (ShiroFilterUtils.isAjax(request)) {
        ShiroFilterUtils.errorRstOut("no login access", ErrorCode.NOTLOGIN, request, response);
        return Boolean.FALSE;
      }

      try {
        if (ShiroFilterUtils.isAppRequest(request)) {
          ShiroFilterUtils.errorRstOut("no login access", ErrorCode.NOTLOGIN, request, response);
        } else {
          WebUtils.getSavedRequest(request);
          WebUtils.issueRedirect(request, response, ShiroFilterUtils.LOGIN_URL);
        }
      } catch (Exception e) {
        logger.warn("WebUtils.issueRedirect error:" + ShiroFilterUtils.LOGIN_URL, e.getMessage());
      }
      return Boolean.FALSE;

    } else {
      //已登录
      if (ShiroFilterUtils.isAjax(request)) {
        ShiroFilterUtils.errorRstOut("no privilege access", ErrorCode.NOTPRIVILEGE, request, response);
        return Boolean.FALSE;
      }
      try {
        if (ShiroFilterUtils.isAppRequest(request)) {
          ShiroFilterUtils.errorRstOut("no privilege access", ErrorCode.NOTPRIVILEGE, request, response);
        } else {
          WebUtils.getSavedRequest(request);
          WebUtils.issueRedirect(request, response, ShiroFilterUtils.UNAUTHORIZED);
        }
      } catch (Exception e) {
        logger.warn("WebUtils.issueRedirect error:" + ShiroFilterUtils.UNAUTHORIZED, e.getMessage());
      }
      return Boolean.FALSE;
    }
  }

}
