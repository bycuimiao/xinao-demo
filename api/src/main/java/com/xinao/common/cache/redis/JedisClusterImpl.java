/*
 * Created by guanshang on 2016-11-08.
 */

package com.xinao.common.cache.redis;


import com.xinao.common.cache.ICache;
import com.xinao.common.cache.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-08
 */
public class JedisClusterImpl implements ICache {
  protected static Logger logger = LoggerFactory.getLogger(JedisClusterImpl.class);

  private JedisCluster jedisCluster;

  public JedisClusterImpl(JedisCluster jedisCluster) {
    this.jedisCluster = jedisCluster;
  }

  @Override
  public Object del(byte[] key) {
    try {
      return this.getJedisCluster().del(key);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("delListByKey error", e);
      }
    }
    return null;
  }

  @Override
  public byte[] get(byte[] key) {
    try {
      return this.getJedisCluster().get(key);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("get error", e);
      }
    }
    return null;
  }


  @Override
  @SuppressWarnings({"unchecked", "varargs"})
  public <T> T get(String key, Class<T>... requiredType) {
    try {
      byte[] skey = key.getBytes();
      return SerializeUtil.deserialize(this.getJedisCluster().get(skey), requiredType);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("get error", e);
      }
    }
    return null;
  }


  @Override
  public Object set(String key, Object value) {
    try {
      byte[] skey = key.getBytes();
      byte[] svalue = SerializeUtil.serialize(value);
      return this.getJedisCluster().set(skey, svalue);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("set error", e);
      }
    }
    return null;
  }


  @Override
  public Object setex(String key, Object value, int expireTime) {
    try {
      byte[] skey = key.getBytes();
      byte[] svalue = SerializeUtil.serialize(value);

      if (expireTime > 0) {
        return this.getJedisCluster().setex(skey, expireTime, svalue);
      } else {
        return this.getJedisCluster().set(skey, svalue);
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("setex error", e);
      }
    }
    return null;
  }


  @Override
  public <T> T getVByMap(String mapkey, String key, Class<T> requiredType) {
    T rst = null;
    try {
      byte[] mkey = mapkey.getBytes();
      byte[] skey = key.getBytes();
      List<byte[]> result = this.getJedisCluster().hmget(mkey, skey);
      if (null != result && result.size() > 0) {
        byte[] xx = result.get(0);
        rst = SerializeUtil.deserialize(xx, requiredType);
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getVByMap error", e);
      }
    }
    return rst;
  }


  @Override
  public Object setVByMap(String mapkey, String key, Object value) {
    try {
      byte[] mkey = mapkey.getBytes();
      byte[] skey = key.getBytes();
      byte[] svalue = SerializeUtil.serialize(value);
      return this.getJedisCluster().hset(mkey, skey, svalue);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("setVByMap error", e);
      }
    }
    return null;
  }

  @Override
  public Object delByMapKey(String mapKey, String... dkey) {
    try {
      byte[][] dx = new byte[dkey.length][];
      for (int i = 0; i < dkey.length; i++) {
        dx[i] = dkey[i].getBytes();
      }
      byte[] mkey = mapKey.getBytes();
      return this.getJedisCluster().hdel(mkey, dx);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("delByMapKey error", e);
      }
    }
    return null;
  }

  @Override
  public <T> Set<T> getVBySet(String setKey, Class<T> requiredType) {
    Set<T> set = null;
    try {
      set = new TreeSet<>();
      byte[] lkey = setKey.getBytes();
      Set<byte[]> xx = this.getJedisCluster().smembers(lkey);
      T rst;
      for (byte[] bs : xx) {
        rst = SerializeUtil.deserialize(bs, requiredType);
        set.add(rst);
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getVBySet error", e);
      }
    }
    return set;
  }


  @Override
  public Long getLenBySet(String setKey) {
    try {
      byte[] lkey = setKey.getBytes();
      return this.getJedisCluster().scard(lkey);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getLenBySet error", e);
      }
    }
    return null;
  }


  @Override
  public Long delSetByKey(String setKey) {
    try {
      byte[] lkey = setKey.getBytes();
      return this.getJedisCluster().del(lkey);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("delSetByKey error", e);
      }
    }
    return null;
  }

  @Override
  public <T> T srandmember(String setKey, Class<T> requiredType) {
    T rst = null;
    try {
      byte[] lkey = setKey.getBytes();
      byte[] svalue = this.getJedisCluster().srandmember(lkey);
      rst = SerializeUtil.deserialize(svalue, requiredType);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("srandmember error", e);
      }
    }
    return rst;
  }


  @Override
  public Object setVBySet(String setKey, Object value) {
    try {
      byte[] lkey = setKey.getBytes();
      byte[] svalue = SerializeUtil.serialize(value);
      return this.getJedisCluster().sadd(lkey, svalue);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("setVBySet error", e);
      }
    }
    return null;
  }


  @Override
  public Object setVByList(String listKey, Object value) {
    try {
      byte[] lkey = listKey.getBytes();
      byte[] svalue = SerializeUtil.serialize(value);
      return this.getJedisCluster().rpush(lkey, svalue);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("setVByList error", e);
      }
    }
    return null;
  }


  @Override
  public <T> List<T> getVByList(String listKey, int start, int end, Class<T> requiredType) {
    List<T> list = null;
    try {
      byte[] lkey = listKey.getBytes();
      list = new ArrayList<>();
      List<byte[]> xx = this.getJedisCluster().lrange(lkey, start, end);
      T rst;
      for (byte[] bs : xx) {
        rst = SerializeUtil.deserialize(bs, requiredType);
        list.add(rst);
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getVByList error", e);
      }
    }
    return list;
  }


  @Override
  public Long getLenByList(String listKey) {
    try {
      byte[] lkey = listKey.getBytes();
      return this.getJedisCluster().llen(lkey);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getLenByList error", e);
      }
    }
    return null;
  }


  @Override
  public Long delListByKey(String listKey) {
    try {
      byte[] lkey = listKey.getBytes();
      return this.getJedisCluster().del(lkey);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("delListByKey error", e);
      }
    }
    return null;
  }


  @Override
  public Long delByKey(String... dkey) {
    try {
      byte[][] dx = new byte[dkey.length][];
      for (int i = 0; i < dkey.length; i++) {
        dx[i] = dkey[i].getBytes();
      }
      return this.getJedisCluster().del(dx);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("delByKey error", e);
      }
    }
    return null;
  }

  @Override
  public boolean exists(String existskey) {
    boolean rst = false;
    try {
      byte[] lkey = existskey.getBytes();
      rst = this.getJedisCluster().exists(lkey);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("exists error", e);
      }
    }

    return rst;
  }


  @Override
  public Set<String> getPatternKeys(String pattern) {
    TreeSet<String> keys = null;
    try {
      Map<String, JedisPool> clusterNodes = this.getJedisCluster().getClusterNodes();
      keys = new TreeSet<String>();
      for (String k : clusterNodes.keySet()) {
        JedisPool jp = clusterNodes.get(k);
        Jedis connection = jp.getResource();
        try {
          keys.addAll(connection.keys(pattern));
        } catch (Exception e) {
          if (logger.isErrorEnabled()) {
            logger.error("getPatternKeys error", e);
          }
        } finally {
          connection.close();// 用完一定要close这个链接！！！
        }
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getPatternKeys error", e);
      }
    }
    return keys;
  }

  @Override
  public Long incr(String key) {
    try {
      byte[] skey = key.getBytes();
      return this.getJedisCluster().incr(key);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("incr error", e);
      }
    }
    return null;
  }

  @Override
  public Long decr(String key) {
    try {
      byte[] skey = key.getBytes();
      return this.getJedisCluster().decr(key);
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("decr error", e);
      }
    }
    return null;
  }

  public JedisCluster getJedisCluster() {
    return jedisCluster;
  }

  public void setJedisCluster(JedisCluster jedisCluster) {
    this.jedisCluster = jedisCluster;
  }

}
