/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.filter;

import com.xinao.entity.User;
import com.xinao.shiro.enums.ErrorCode;
import com.xinao.shiro.token.manager.TokenManager;
import org.apache.shiro.SecurityUtils;
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
public class LoginFilter extends AccessControlFilter {
  private Logger logger = LoggerFactory.getLogger(LoginFilter.class);

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    Subject subject = SecurityUtils.getSubject();
    User token = (User) subject.getPrincipal();

    if (null != token || isLoginRequest(request, response)) {
      //判断用户是通过记住我功能自动登录,此时session失效-需要维护session.
      if (!subject.isAuthenticated() && subject.isRemembered()) {
        logger.info("current user login with remembered me");
        TokenManager.login(token, true);
      }
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    if (ShiroFilterUtils.isAjax(request)) {
      ShiroFilterUtils.errorRstOut("current user no login or logout", ErrorCode.NOTLOGIN, request, response);
      return Boolean.FALSE;
    }

    try {
      if (ShiroFilterUtils.isAppRequest(request)) {
        ShiroFilterUtils.errorRstOut("current user no login or logout", ErrorCode.NOTLOGIN, request, response);
      } else {
        WebUtils.getSavedRequest(request);
        WebUtils.issueRedirect(request, response, ShiroFilterUtils.LOGIN_URL);
      }
    } catch (Exception e) {
      logger.warn("WebUtils.issueRedirect error :" + ShiroFilterUtils.LOGIN_URL, e.getMessage());
    }
    return Boolean.FALSE;
  }
}
