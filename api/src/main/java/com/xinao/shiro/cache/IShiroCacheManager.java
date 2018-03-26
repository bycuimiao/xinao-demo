/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.cache;

import org.apache.shiro.cache.Cache;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public interface IShiroCacheManager {

  <K, V> Cache<K, V> getCache(String name);

  void destroy();

}
