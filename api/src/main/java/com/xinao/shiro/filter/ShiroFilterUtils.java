/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.xinao.common.Result;
import com.xinao.common.State;
import com.xinao.shiro.util.LoggerUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;

import java.io.PrintWriter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class ShiroFilterUtils {
  //登录页面.
  public static final String LOGIN_URL = "/sys/login";
  //踢出登录提示.
  public static final String KICKED_OUT = "/sys/kickedout";
  //没有权限提醒.
  public static final String UNAUTHORIZED = "/sys/unauthorized";

  /**
   * 是否是Ajax请求.
   *
   * @param request request
   * @return 是否
   */
  public static boolean isAjax(ServletRequest request) {
    return "XMLHttpRequest".equalsIgnoreCase(((HttpServletRequest) request).getHeader("X-Requested-With"));
  }

  /**
   * 是否app请求.
   *
   * @param request request
   * @return 是否
   */
  public static boolean isAppRequest(ServletRequest request) {
    return true;
  }


  /**
   * 错误信息输出.
   *
   * @param message  错误信息
   * @param data     错误数据
   * @param request  request
   * @param response response
   */
  public static void errorRstOut(String message, Object data, ServletRequest request, ServletResponse response) {
    try {
      Result<Object, State> result = new Result<>();
      result.setCode(State.FAILED);
      result.setMessage(message);
      if (data != null) {
        result.setData(data);
      }

      //清空浏览器缓存
      HttpServletRequest hsRequest = (HttpServletRequest) request;
      HttpServletResponse hsResponse = (HttpServletResponse) response;
      resetCookies(hsRequest, hsResponse);

      out(response, result);

    } catch (Exception e) {
      LoggerUtils.error(ShiroFilterUtils.class, "errorRstOut error", e);
    }
  }

  /**
   * 重置cookie.
   *
   * @param hsRequest  hsRequest
   * @param hsResponse hsResponse
   */
  public static void resetCookies(HttpServletRequest hsRequest, HttpServletResponse hsResponse) {
    Cookie[] cookies = hsRequest.getCookies();
    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        if ("v_v-s-api".equals(cookie.getName()) || "v_v-re-api".equals(cookie.getName())) {
          cookie.setValue(null);
          cookie.setMaxAge(0);// 立即销毁cookie
          cookie.setPath("/");
          hsResponse.addCookie(cookie);
          break;
        }
      }
    }
  }

  /**
   * response 输出JSON.
   *
   * @param response response
   * @param result   输出结果
   */
  public static void out(ServletResponse response, Object result) {
    PrintWriter out = null;
    try {
      ShiroHttpServletResponse res = (ShiroHttpServletResponse) response;
      res.addHeader("Access-Control-Allow-Origin", "*");
      res.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
      res.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
      res.addHeader("Access-Control-Max-Age", "1728000");

      res.setHeader("content-type", "text/html;charset=UTF-8");
      res.setCharacterEncoding("UTF-8");
      out = res.getWriter();
      out.println(JSON.toJSONString(result));
    } catch (Exception e) {
      LoggerUtils.error(ShiroFilterUtils.class, "reponse error", e);
    } finally {
      if (null != out) {
        out.flush();
        out.close();
      }
    }
  }
}
