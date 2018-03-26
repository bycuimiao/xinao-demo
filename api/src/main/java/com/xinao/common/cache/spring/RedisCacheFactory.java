/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.common.cache.spring;


import com.xinao.common.cache.ICache;
import com.xinao.common.cache.redis.JedisClusterImpl;
import com.xinao.common.cache.redis.JedisSingleImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class RedisCacheFactory {
  protected static Logger logger = LoggerFactory.getLogger(RedisCacheFactory.class);

  private RedisCacheFactory() {
  }

  /**
   * 获取redis cache.
   *
   * @param redisObj 注入实体：单机或者集群
   * @return caceh工具类
   */
  public static ICache getInstance(Object redisObj) {
    ICache cache = null;
    if (redisObj instanceof JedisPool) {
      JedisPool jedisPool = null;
      try {
        jedisPool = (JedisPool) redisObj;
      } catch (BeansException e) {
        if (logger.isErrorEnabled()) {
          logger.error("try spring jedisPool error", e);
        }
      }
      if (jedisPool != null) {
        cache = new JedisSingleImpl(jedisPool);
      }
    } else if (redisObj instanceof JedisCluster) {
      JedisCluster jedisCluster = null;
      try {
        jedisCluster = (JedisCluster) redisObj;
      } catch (BeansException e) {
        if (logger.isErrorEnabled()) {
          logger.error("try spring jedisCluster error", e);
        }
      }
      if (jedisCluster != null) {
        cache = new JedisClusterImpl(jedisCluster);
      }

    }
    return cache;
  }
}
