/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.cache.impl;

import com.xinao.shiro.cache.IShiroCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.Destroyable;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class CustomShiroCacheManager implements CacheManager, Destroyable {

  private IShiroCacheManager shiroCacheManager;

  @Override
  public <K, V> Cache<K, V> getCache(String name) throws CacheException {
    return getShiroCacheManager().getCache(name);
  }

  @Override
  public void destroy() throws Exception {
    shiroCacheManager.destroy();
  }

  public IShiroCacheManager getShiroCacheManager() {
    return shiroCacheManager;
  }

  public void setShiroCacheManager(IShiroCacheManager shiroCacheManager) {
    this.shiroCacheManager = shiroCacheManager;
  }
}
