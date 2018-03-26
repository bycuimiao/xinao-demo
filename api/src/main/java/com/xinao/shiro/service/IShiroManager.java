/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.service;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public interface IShiroManager {

  /**
   * 加载过滤配置信息.
   *
   * @return 加载的配置文件
   */
  public String loadFilterChainDefinitions();

  /**
   * 重新构建权限过滤器
   * 一般在修改了用户角色、用户等信息时，需要再次调用该方法.
   */
  public void reCreateFilterChains();
}
