/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.cache.impl;

import com.xinao.shiro.cache.IShiroCacheManager;
import com.xinao.shiro.cache.JedisManager;
import com.xinao.shiro.cache.JedisShiroCache;
import org.apache.shiro.cache.Cache;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class JedisShiroCacheManager implements IShiroCacheManager {

  private JedisManager jedisManager;

  @Override
  public <K, V> Cache<K, V> getCache(String name) {
    return new JedisShiroCache<K, V>(name, getJedisManager());
  }

  @Override
  public void destroy() {
    //如果和其他系统，或者应用在一起就不能关闭
    //getJedisManager().getJedis().shutdown();
  }

  public JedisManager getJedisManager() {
    return jedisManager;
  }

  public void setJedisManager(JedisManager jedisManager) {
    this.jedisManager = jedisManager;
  }
}
