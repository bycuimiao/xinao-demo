/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.common;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public interface ShiroConstant {

  /**
   * session存储key.
   */
  String REDIS_SHIRO_SESSION = "zhwl-shiro-api-session:";
  /**
   * 模糊匹配session.
   */
  String REDIS_SHIRO_ALL = "*zhwl-shiro-api-session:*";


  /**
   * 为了不和其他的缓存混淆，采用追加前缀方式以作区分.
   */
  String REDIS_SHIRO_CACHE = "zhwl-shiro-api-cache:";

  /**
   * session状态.
   */
  String SESSION_STATUS = "session-status";
  /**
   * session过期时间:timeout+SESSION_VAL_TIME_SPAN+5*60.
   */
  int SESSION_VAL_TIME_SPAN = 18000;
  /**
   * 存储在线用户.
   */
  String ONLINE_USER = "kickout-session-online_user";
  /**
   * 分割符号.
   */
  String SPLIT = "_";


}
