/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.cache;

import com.xinao.common.cache.ICache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class JedisManager {
  protected static Logger logger = LoggerFactory.getLogger(JedisManager.class);

  private ICache redisCache;

  public byte[] getValueByKey(byte[] key) {
    return redisCache.get(key);
  }

  @SuppressWarnings({"unchecked", "varargs"})
  public <T> T get(String key, Class<T>... requiredType) {
    return redisCache.get(key, requiredType);
  }

  public void delByKey(String... dkey) {
    redisCache.delByKey(dkey);
  }

  public void saveValueByKey(String key, Object value, int expireTime) {
    redisCache.setex(key, value, expireTime);
  }

  public Set<String> getPatternKeys(String pattern) {
    return redisCache.getPatternKeys(pattern);
  }

  public ICache getRedisCache() {
    return redisCache;
  }

  public void setRedisCache(ICache redisCache) {
    this.redisCache = redisCache;
  }

}
