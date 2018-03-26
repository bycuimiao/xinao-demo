/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.filter;

import com.xinao.shiro.enums.ErrorCode;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class RoleFilter extends AccessControlFilter {
  private Logger logger = LoggerFactory.getLogger(RoleFilter.class);

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    String[] arra = (String[]) mappedValue;

    Subject subject = getSubject(request, response);
    for (String role : arra) {
      if (subject.hasRole("role:" + role)) {
        return Boolean.TRUE;
      }
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
          return Boolean.FALSE;
        } else {
          WebUtils.saveRequest(request);
          WebUtils.issueRedirect(request, response, ShiroFilterUtils.LOGIN_URL);
        }
      } catch (Exception e) {
        logger.warn("WebUtils.issueRedirect error:" + ShiroFilterUtils.LOGIN_URL, e.getMessage());
      }
    } else {
      if (ShiroFilterUtils.isAjax(request)) {
        ShiroFilterUtils.errorRstOut("no privilege access", ErrorCode.NOTPRIVILEGE, request, response);
        return Boolean.FALSE;
      }
      try {
        if (ShiroFilterUtils.isAppRequest(request)) {
          ShiroFilterUtils.errorRstOut("no privilege access", ErrorCode.NOTPRIVILEGE, request, response);
          return Boolean.FALSE;
        } else {
          WebUtils.saveRequest(request);
          WebUtils.issueRedirect(request, response, ShiroFilterUtils.UNAUTHORIZED);
        }
      } catch (Exception e) {
        logger.warn("WebUtils.issueRedirect error:" + ShiroFilterUtils.UNAUTHORIZED, e.getMessage());
      }
    }
    return Boolean.FALSE;
  }
}
