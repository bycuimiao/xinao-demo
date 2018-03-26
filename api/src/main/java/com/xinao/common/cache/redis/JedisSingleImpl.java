/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.common.cache.redis;


import com.xinao.common.cache.ICache;
import com.xinao.common.cache.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class JedisSingleImpl implements ICache {
  protected static Logger logger = LoggerFactory.getLogger(JedisSingleImpl.class);

  private JedisPool jedisPool;

  public JedisSingleImpl(JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }

  @Override
  public byte[] get(byte[] key) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      return jds.get(key);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("get error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  @SuppressWarnings({"unchecked", "varargs"})
  public <T> T get(String key, Class<T>... requiredType) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] skey = key.getBytes();
      return SerializeUtil.deserialize(jds.get(skey), requiredType);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("get error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public Object del(byte[] key) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      return jds.del(key);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("del error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }


  @Override
  public Object set(String key, Object value) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] skey = key.getBytes();
      byte[] svalue = SerializeUtil.serialize(value);
      return jds.set(skey, svalue);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("set error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }

    return null;
  }

  @Override
  public Object setex(String key, Object value, int expireTime) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] skey = key.getBytes();
      byte[] svalue = SerializeUtil.serialize(value);

      if (expireTime > 0) {
        return jds.setex(skey, expireTime, svalue);
      } else {
        return jds.set(skey, svalue);
      }
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("setex error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  @SuppressWarnings({"unchecked", "varargs"})
  public <T> T getVByMap(String mapkey, String key, Class<T> requiredType) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] mkey = mapkey.getBytes();
      byte[] skey = key.getBytes();
      List<byte[]> result = jds.hmget(mkey, skey);
      if (null != result && result.size() > 0) {
        byte[] xx = result.get(0);
        return SerializeUtil.deserialize(xx, requiredType);
      }
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("getVByMap error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public Object setVByMap(String mapkey, String key, Object value) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] mkey = mapkey.getBytes();
      byte[] skey = key.getBytes();
      byte[] svalue = SerializeUtil.serialize(value);
      return jds.hset(mkey, skey, svalue);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("getVByMap error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }

    return null;
  }

  @Override
  public Object delByMapKey(String mapKey, String... dkey) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[][] dx = new byte[dkey.length][];
      for (int i = 0; i < dkey.length; i++) {
        dx[i] = dkey[i].getBytes();
      }
      byte[] mkey = mapKey.getBytes();
      return jds.hdel(mkey, dx);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("delByMapKey error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  @SuppressWarnings({"unchecked", "varargs"})
  public <T> Set<T> getVBySet(String setKey, Class<T> requiredType) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] lkey = setKey.getBytes();
      Set<T> set = new TreeSet<>();
      Set<byte[]> xx = jds.smembers(lkey);
      T rst;
      for (byte[] bs : xx) {
        rst = SerializeUtil.deserialize(bs, requiredType);
        set.add(rst);
      }
      return set;
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("getVBySet error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public Long getLenBySet(String setKey) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      return jds.scard(setKey);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("getLenBySet error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public Long delSetByKey(String setKey) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      return jds.del(setKey);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("delSetByKey error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  @SuppressWarnings({"unchecked", "varargs"})
  public <T> T srandmember(String setKey, Class<T> requiredType) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] lkey = setKey.getBytes();
      byte[] xx = jds.srandmember(lkey);

      return SerializeUtil.deserialize(xx, requiredType);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("srandmember error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public Object setVBySet(String setKey, Object value) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] skey = setKey.getBytes();
      byte[] svalue = SerializeUtil.serialize(value);
      return jds.sadd(skey, svalue);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("setVBySet error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public Object setVByList(String listKey, Object value) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] lkey = listKey.getBytes();
      byte[] svalue = SerializeUtil.serialize(value);
      return jds.rpush(lkey, svalue);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("setVByList error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  @SuppressWarnings({"unchecked", "varargs"})
  public <T> List<T> getVByList(String listKey, int start, int end, Class<T> requiredType) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] lkey = listKey.getBytes();
      List<T> list = new ArrayList<>();
      List<byte[]> xx = jds.lrange(lkey, start, end);
      T rst;
      for (byte[] bs : xx) {
        rst = SerializeUtil.deserialize(bs, requiredType);
        list.add(rst);
      }
      return list;
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("setVByList error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public Long getLenByList(String listKey) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] lkey = listKey.getBytes();
      return jds.llen(lkey);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("JedisSingleUtils.getLenByList error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public Long delListByKey(String listKey) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] lkey = listKey.getBytes();
      return jds.del(lkey);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("delListByKey error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public Long delByKey(String... dkey) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[][] dx = new byte[dkey.length][];
      for (int i = 0; i < dkey.length; i++) {
        dx[i] = dkey[i].getBytes();
      }
      return jds.del(dx);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("delByKey error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public boolean exists(String existskey) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      byte[] lkey = existskey.getBytes();
      return jds.exists(lkey);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("JedisSingleUtils.exists error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return false;
  }

  @Override
  public Set<String> getPatternKeys(String pattern) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      return jds.keys((pattern));
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("getPatternKeys error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public Long incr(String key) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      return jds.incr(key);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("incr error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  @Override
  public Long decr(String key) {
    Jedis jds = null;
    boolean isBroken = false;
    try {
      jds = this.getJedis();
      return jds.decr(key);
    } catch (Exception e) {
      isBroken = true;
      if (logger.isErrorEnabled()) {
        logger.error("decr error", e);
      }
    } finally {
      returnResource(jds, isBroken);
    }
    return null;
  }

  /**
   * 释放资源.
   *
   * @param jedis    jedis
   * @param isBroken 是否中断
   */
  public void returnResource(Jedis jedis, boolean isBroken) {
    if (jedis == null) {
      return;
    }
    jedis.close();
  }


  /**
   * 获取redis链接.
   *
   * @return jedis
   */
  public Jedis getJedis() {
    Jedis jedis;
    try {
      jedis = this.getJedisPool().getResource();
    } catch (Exception e) {
      throw new JedisConnectionException(e);
    }
    return jedis;
  }

  public JedisPool getJedisPool() {
    return jedisPool;
  }

  public void setJedisPool(JedisPool jedisPool) {
    this.jedisPool = jedisPool;
  }
}
