/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class LoggerUtils {
  protected static Logger logger = LoggerFactory.getLogger(LoggerUtils.class);

  /**
   * 是否开启Debug.
   */
  public static boolean isDebug = logger.isDebugEnabled();
  /**
   * 是否开启info.
   */
  public static boolean isInfo = logger.isInfoEnabled();
  /**
   * 是否开启warn.
   */
  public static boolean isWarn = logger.isWarnEnabled();
  /**
   * 是否开启error.
   */
  public static boolean isError = logger.isErrorEnabled();

  /**
   * Debug 输出.
   *
   * @param clazz   目标.Class
   * @param message 输出信息
   */
  public static void debug(Class<? extends Object> clazz, String message) {
    if (!isDebug) {
      return;
    }
    Logger logger = LoggerFactory.getLogger(clazz);
    logger.debug(message);
  }

  /**
   * Debug 输出.
   *
   * @param clazz     目标.Class
   * @param fmtString 输出信息key
   * @param value     输出信息value
   */
  public static void fmtDebug(Class<? extends Object> clazz, String fmtString, Object... value) {
    if (!isDebug) {
      return;
    }
    if (StringUtils.isBlank(fmtString)) {
      return;
    }
    if (null != value && value.length != 0) {
      fmtString = String.format(fmtString, value);
    }
    debug(clazz, fmtString);
  }

  /**
   * warn 输出.
   *
   * @param clazz   目标.Class
   * @param message 输出信息
   */
  public static void warn(Class<? extends Object> clazz, String message) {
    if (!isWarn) {
      return;
    }
    Logger logger = LoggerFactory.getLogger(clazz);
    logger.warn(message);
  }

  /**
   * warn 输出.
   *
   * @param clazz     目标.Class
   * @param fmtString 输出信息key
   * @param value     输出信息value
   */
  public static void fmtWarn(Class<? extends Object> clazz, String fmtString, Object... value) {
    if (!isWarn) {
      return;
    }
    if (StringUtils.isBlank(fmtString)) {
      return;
    }
    if (null != value && value.length != 0) {
      fmtString = String.format(fmtString, value);
    }
    warn(clazz, fmtString);
  }

  /**
   * warn 输出.
   *
   * @param clazz   目标.Class
   * @param message 输出信息
   */
  public static void info(Class<? extends Object> clazz, String message) {
    if (!isInfo) {
      return;
    }
    Logger logger = LoggerFactory.getLogger(clazz);
    logger.info(message);
  }

  /**
   * warn 输出.
   *
   * @param clazz     目标.Class
   * @param fmtString 输出信息key
   * @param value     输出信息value
   */
  public static void fmtInfo(Class<? extends Object> clazz, String fmtString, Object... value) {
    if (!isInfo) {
      return;
    }
    if (StringUtils.isBlank(fmtString)) {
      return;
    }
    if (null != value && value.length != 0) {
      fmtString = String.format(fmtString, value);
    }
    info(clazz, fmtString);
  }


  /**
   * Error 输出.
   *
   * @param clazz     目标.Class
   * @param message   输出信息
   * @param exception 异常类
   */
  public static void error(Class<? extends Object> clazz, String message, Exception exception) {
    if (!isError) {
      return;
    }
    Logger logger = LoggerFactory.getLogger(clazz);
    if (null == exception) {
      logger.error(message);
      return;
    }
    logger.error(message, exception);
  }

  /**
   * Error 输出.
   *
   * @param clazz   目标.Class
   * @param message 输出信息
   */
  public static void error(Class<? extends Object> clazz, String message) {
    if (!isError) {
      return;
    }
    error(clazz, message, null);
  }

  /**
   * 异常填充值输出.
   *
   * @param clazz     目标.Class
   * @param fmtString 输出信息key
   * @param exception 异常类
   * @param value     输出信息value
   */
  public static void fmtError(Class<? extends Object> clazz, Exception exception, String fmtString, Object... value) {
    if (!isError) {
      return;
    }
    if (StringUtils.isBlank(fmtString)) {
      return;
    }
    if (null != value && value.length != 0) {
      fmtString = String.format(fmtString, value);
    }
    error(clazz, fmtString, exception);
  }

  /**
   * 异常填充值输出.
   *
   * @param clazz     目标.Class
   * @param fmtString 输出信息key
   * @param value     输出信息value
   */
  public static void fmtError(Class<? extends Object> clazz, String fmtString, Object... value) {
    if (!isError) {
      return;
    }
    if (StringUtils.isBlank(fmtString)) {
      return;
    }
    if (null != value && value.length != 0) {
      fmtString = String.format(fmtString, value);
    }
    error(clazz, fmtString);
  }
}
