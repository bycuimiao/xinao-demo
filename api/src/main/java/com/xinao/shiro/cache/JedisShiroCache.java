/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.cache;

import com.xinao.common.cache.util.SerializeUtil;
import com.xinao.entity.User;
import com.xinao.shiro.common.ShiroConstant;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class JedisShiroCache<K, V> implements Cache<K, V> {
  protected static Logger logger = LoggerFactory.getLogger(JedisShiroCache.class);

  private JedisManager jedisManager;

  private String name;

  public JedisShiroCache(String name, JedisManager jedisManager) {
    this.name = name;
    this.jedisManager = jedisManager;
  }

  /**
   * 自定义relm中的授权/认证的类名加上授权/认证英文名字.
   *
   * @return String 名字
   */
  public String getName() {
    if (name == null) {
      return "";
    }
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  @SuppressWarnings("unchecked")
  public V get(K key) {
    byte[] byteValue = new byte[0];
    try {
      byteValue = jedisManager.getValueByKey(buildCacheKey(key).getBytes());
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("get value by cache throw exception", e);
      }
    }
    return (V) SerializeUtil.deserialize(byteValue);
  }

  @Override
  public V put(K key, V value) {
    V previos = get(key);
    try {
      jedisManager.saveValueByKey(buildCacheKey(key), value, 1000 * 60 * 24 * 7);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("put cache throw exception", e);
      }
    }
    return previos;
  }

  @Override
  public V remove(K key) {
    V previos = get(key);
    try {
      jedisManager.delByKey(buildCacheKey(key));
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("remove cache  throw exception", e);
      }
    }
    return previos;
  }


  @Override
  public int size() {
    if (keys() == null) {
      return 0;
    }
    return keys().size();
  }

  @Override
  public void clear() throws CacheException {
  }

  @Override
  public Set<K> keys() {
    return null;
  }

  @Override
  public Collection<V> values() {
    return null;
  }

  private String buildCacheKey(Object key) {
    SimplePrincipalCollection spc = (SimplePrincipalCollection) key;
    //判断用户，匹配用户ID。
    User user = null;
    Object obj = spc.getPrimaryPrincipal();
    if (obj != null) {
      if (obj instanceof SimplePrincipalCollection) {
        Object realObj = ((SimplePrincipalCollection) obj).getPrimaryPrincipal();
        if (realObj instanceof User) {
          user = (User) realObj;
        }
      } else if (obj instanceof User) {
        user = (User) obj;
      }
    }

    if (user != null) {
      return ShiroConstant.REDIS_SHIRO_CACHE + getName() + ":" + user.getId() + user.getCreateTime().getTime();
    } else {
      return ShiroConstant.REDIS_SHIRO_CACHE + getName() + ":" + key;
    }
  }

}
