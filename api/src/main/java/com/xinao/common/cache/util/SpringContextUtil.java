/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.common.cache.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class SpringContextUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringContextUtil.applicationContext = applicationContext;
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /**
   * 获取spring管理bean.
   *
   * @param name name
   * @return spring管理的bean
   * @throws BeansException BeansException
   */
  public static Object getBean(String name) throws BeansException {
    try {
      return applicationContext.getBean(name);
    } catch (Exception e) {
      //throw new Exception("get bean error：" + name, e);
    }
    return null;
  }

  public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
    return applicationContext.getBean(name, requiredType);
  }

  public static boolean containsBean(String name) {
    return applicationContext.containsBean(name);
  }

  public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
    return applicationContext.isSingleton(name);
  }

  public static Class<? extends Object> getType(String name) throws NoSuchBeanDefinitionException {
    return applicationContext.getType(name);
  }

  public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
    return applicationContext.getAliases(name);
  }
}
