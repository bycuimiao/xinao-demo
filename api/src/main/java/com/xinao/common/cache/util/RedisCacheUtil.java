/*
 * Created by guanshang on 2016-11-15.
 */

package com.xinao.common.cache.util;


import com.xinao.common.cache.ICache;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-15
 */
public class RedisCacheUtil {
  protected static Logger logger = LoggerFactory.getLogger(RedisCacheUtil.class);

  //获取当前cache
  public static final ICache cache = SpringContextUtil.getBean("redisCache", ICache.class);

  /**
   * cache set.
   *
   * @param businessKey 业务key
   * @param key         key值
   * @param value       value值
   * @return 是否成功
   */
  public static boolean set(String businessKey, String key, Object value) {
    boolean success = false;
    try {
      String redisKey = buildKey(businessKey, key);
      if (StringUtils.isNotBlank(redisKey)) {
        //cache.set(redisKey, value);
        //设置默认时间-1周(week)=604800秒(s).
        cache.setex(redisKey, value, 604800);
        success = true;
      } else {
        success = false;
        if (logger.isErrorEnabled()) {
          logger.error("set error: buildKey error");
        }
      }
    } catch (Exception e) {
      success = false;
      if (logger.isErrorEnabled()) {
        logger.error("set error:", e);
      }
    }
    return success;
  }

  /**
   * cache setex.
   *
   * @param businessKey 业务key
   * @param key         key值
   * @param value       value值
   * @param expireTime  过期时间-秒
   * @return 是否成功
   */
  public static boolean setex(String businessKey, String key, Object value, int expireTime) {
    boolean success = false;
    try {
      String redisKey = buildKey(businessKey, key);
      if (StringUtils.isNotBlank(redisKey)) {
        cache.setex(redisKey, value, expireTime);
        success = true;
      } else {
        success = false;
        if (logger.isErrorEnabled()) {
          logger.error("setex error: buildKey error");
        }
      }
    } catch (Exception e) {
      success = false;
      if (logger.isErrorEnabled()) {
        logger.error("setex error:", e);
      }
    }
    return success;
  }

  /**
   * cache get.
   *
   * @param businessKey  业务key
   * @param key          key值
   * @param requiredType 返回泛型
   * @param <T>          泛型类型
   * @return 返回泛型类型
   */
  @SuppressWarnings({"unchecked", "varargs"})
  public static <T> T get(String businessKey, String key, Class<T>... requiredType) {
    T rst = null;
    try {
      String redisKey = buildKey(businessKey, key);
      if (StringUtils.isNotBlank(redisKey)) {
        rst = cache.get(redisKey, requiredType);
      } else {
        if (logger.isErrorEnabled()) {
          logger.error("get error: buildKey error");
        }
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("get error:", e);
      }
    }
    return rst;
  }

  /**
   * cache set value by map.
   *
   * @param businessKey 业务key
   * @param mapkey      map
   * @param key         key值
   * @param value       value值
   * @return 是否成功
   */
  public static boolean setVByMap(String businessKey, String mapkey, String key, Object value) {
    boolean success = false;
    try {
      String redisKey = buildKey(businessKey, mapkey);
      if (StringUtils.isNotBlank(redisKey)) {
        cache.setVByMap(redisKey, key, value);
        success = true;
      } else {
        success = false;
        if (logger.isErrorEnabled()) {
          logger.error("setVByMap error: buildKey error");
        }
      }
    } catch (Exception e) {
      success = false;
      if (logger.isErrorEnabled()) {
        logger.error("setVByMap error:", e);
      }
    }
    return success;
  }

  /**
   * cache get value by map.
   *
   * @param businessKey  业务key
   * @param mapkey       map
   * @param key          key值
   * @param requiredType 泛型类型
   * @param <T>          泛型类型
   * @return 返回泛型类
   */
  public static <T> T getVByMap(String businessKey, String mapkey, String key, Class<T> requiredType) {
    T rst = null;
    try {
      String redisKey = buildKey(businessKey, mapkey);
      if (StringUtils.isNotBlank(redisKey)) {
        rst = cache.getVByMap(redisKey, key, requiredType);
      } else {
        if (logger.isErrorEnabled()) {
          logger.error("getVByMap error: buildKey error");
        }
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getVByMap error:", e);
      }
    }
    return rst;
  }

  /**
   * cache del by map key.
   *
   * @param businessKey 业务key
   * @param mapKey      map
   * @param dkey        删除key值
   * @return 是否成功
   */
  public static boolean delByMapKey(String businessKey, String mapKey, String... dkey) {
    boolean success = false;
    try {
      String redisKey = buildKey(businessKey, mapKey);
      if (StringUtils.isNotBlank(redisKey)) {
        cache.delByMapKey(redisKey, dkey);
        success = true;
      } else {
        success = false;
        if (logger.isErrorEnabled()) {
          logger.error("delByMapKey error: buildKey error");
        }
      }
    } catch (Exception e) {
      success = false;
      if (logger.isErrorEnabled()) {
        logger.error("delByMapKey error:", e);
      }
    }
    return success;

  }

  /**
   * cache get value by set.
   *
   * @param businessKey  业务key
   * @param setKey       set
   * @param requiredType 泛型类型
   * @param <T>          泛型类型
   * @return 返回泛型类
   */
  public static <T> Set<T> getVBySet(String businessKey, String setKey, Class<T> requiredType) {
    Set<T> set = null;
    try {
      String redisKey = buildKey(businessKey, setKey);
      if (StringUtils.isNotBlank(redisKey)) {
        set = cache.getVBySet(redisKey, requiredType);
      } else {
        if (logger.isErrorEnabled()) {
          logger.error("getVBySet error: buildKey error");
        }
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getVBySet error:", e);
      }
    }
    return set;
  }

  /**
   * get length by set.
   *
   * @param businessKey 业务key
   * @param setKey      set
   * @return length
   */
  public static Long getLenBySet(String businessKey, String setKey) {
    Long rst = 0L;
    try {
      String redisKey = buildKey(businessKey, setKey);
      if (StringUtils.isNotBlank(redisKey)) {
        rst = cache.getLenBySet(redisKey);
      } else {
        if (logger.isErrorEnabled()) {
          logger.error("getLenBySet error: buildKey error");
        }
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getLenBySet error:", e);
      }
    }
    return rst;
  }


  /**
   * set value by set.
   *
   * @param businessKey 业务key
   * @param setKey      set
   * @param value       value
   * @return 是否成功
   */
  public static boolean setVBySet(String businessKey, String setKey, Object value) {
    boolean success = false;
    try {
      String redisKey = buildKey(businessKey, setKey);
      if (StringUtils.isNotBlank(redisKey)) {
        cache.setVBySet(redisKey, value);
        success = true;
      } else {
        success = false;
        if (logger.isErrorEnabled()) {
          logger.error("setVBySet error: buildKey error");
        }
      }
    } catch (Exception e) {
      success = false;
      if (logger.isErrorEnabled()) {
        logger.error("setVBySet error:", e);
      }
    }
    return success;
  }

  /**
   * set value by list.
   *
   * @param businessKey 业务key
   * @param listKey     list
   * @param value       value
   * @return 是否成功
   */
  public static boolean setVByList(String businessKey, String listKey, Object value) {
    boolean success = false;
    try {
      String redisKey = buildKey(businessKey, listKey);
      if (StringUtils.isNotBlank(redisKey)) {
        cache.setVByList(redisKey, value);
        success = true;
      } else {
        success = false;
        if (logger.isErrorEnabled()) {
          logger.error("setVByList error: buildKey error");
        }
      }
    } catch (Exception e) {
      success = false;
      if (logger.isErrorEnabled()) {
        logger.error("setVByList error:", e);
      }
    }
    return success;
  }

  /**
   * get value by list.
   *
   * @param businessKey  业务key
   * @param listKey      list
   * @param start        start
   * @param end          end
   * @param requiredType 泛型类型
   * @param <T>          泛型类型
   * @return 泛型类
   */
  public static <T> List<T> getVByList(String businessKey, String listKey, int start, int end, Class<T> requiredType) {
    List<T> list = null;
    try {
      String redisKey = buildKey(businessKey, listKey);
      if (StringUtils.isNotBlank(redisKey)) {
        list = cache.getVByList(redisKey, start, end, requiredType);
      } else {
        if (logger.isErrorEnabled()) {
          logger.error("getVByList error: buildKey error");
        }
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getVByList error:", e);
      }
    }
    return list;
  }

  /**
   * get length by list.
   *
   * @param businessKey 业务key
   * @param listKey     list
   * @return length
   */
  public static Long getLenByList(String businessKey, String listKey) {
    Long rst = 0L;
    try {
      String redisKey = buildKey(businessKey, listKey);
      if (StringUtils.isNotBlank(redisKey)) {
        rst = cache.getLenByList(redisKey);
      } else {
        if (logger.isErrorEnabled()) {
          logger.error("getLenByList error: buildKey error");
        }
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getLenByList error:", e);
      }
    }
    return rst;
  }

  /**
   * cache exists.
   *
   * @param businessKey 业务key
   * @param existskey   existskey
   * @return 是否存在
   */
  public static boolean exists(String businessKey, String existskey) {
    boolean success = false;
    try {
      String redisKey = buildKey(businessKey, existskey);
      if (StringUtils.isNotBlank(redisKey)) {
        return cache.exists(redisKey);
      } else {
        success = false;
        if (logger.isErrorEnabled()) {
          logger.error("exists error: buildKey error");
        }
      }
    } catch (Exception e) {
      success = false;
      if (logger.isErrorEnabled()) {
        logger.error("exists error:", e);
      }
    }
    return success;
  }

  /**
   * cache del by key.
   *
   * @param businessKey 业务key
   * @param dkey        删除key
   * @return 是否成功
   */
  public static boolean delByKey(String businessKey, String... dkey) {
    boolean success = false;
    try {
      if (StringUtils.isNotBlank(businessKey)) {
        String[] redisKeyArr = new String[dkey.length];
        for (int i = 0; i < dkey.length; i++) {
          redisKeyArr[i] = buildKey(businessKey, dkey[i]);
        }
        cache.delByKey(redisKeyArr);
        success = true;
      } else {
        success = false;
        if (logger.isErrorEnabled()) {
          logger.error("delByKey error: buildKey error");
        }
      }
    } catch (Exception e) {
      success = false;
      if (logger.isErrorEnabled()) {
        logger.error("delByKey error:", e);
      }
    }
    return success;
  }

  /**
   * cache pattern keys.
   *
   * @param businessKey 业务key
   * @param pattern     匹配规则
   * @return 匹配到的key
   */
  public static Set<String> getPatternKeys(String businessKey, String pattern) {
    Set<String> set = null;
    try {
      String redisKey = buildKey(businessKey, pattern);
      if (StringUtils.isNotBlank(redisKey)) {
        set = cache.getPatternKeys(redisKey);
      } else {
        if (logger.isErrorEnabled()) {
          logger.error("getPatternKeys error: buildKey error");
        }
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("getPatternKeys error:", e);
      }
    }
    return set;
  }

  /**
   * 获取自增id.
   *
   * @param businessKey 业务key
   * @param key         key
   * @return 得到的自增id
   */
  public static Long incr(String businessKey, String key) {
    Long result = null;
    try {
      String redisKey = buildKey(businessKey, key);
      if (StringUtils.isNotBlank(redisKey)) {
        result = cache.incr(redisKey);
      } else {
        if (logger.isErrorEnabled()) {
          logger.error("incr error: buildKey error");
        }
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("incr error:", e);
      }
    }
    return result;
  }

  /**
   * 获取自减id.
   *
   * @param businessKey 业务key
   * @param key         key
   * @return 得到的自减id
   */
  public static Long decr(String businessKey, String key) {
    Long result = null;
    try {
      String redisKey = buildKey(businessKey, key);
      if (StringUtils.isNotBlank(redisKey)) {
        result = cache.decr(redisKey);
      } else {
        if (logger.isErrorEnabled()) {
          logger.error("decr error: buildKey error");
        }
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("decr error:", e);
      }
    }
    return result;
  }

  private static String buildKey(String businessKey, String key) {
    if (StringUtils.isBlank(businessKey)) {
      if (logger.isErrorEnabled()) {
        logger.error("businessKey is bank error:");
      }
      return null;
    } else if (StringUtils.isBlank(key)) {
      if (logger.isErrorEnabled()) {
        logger.error("key is bank error:");
      }
      return null;
    }
    return businessKey + "-" + key;
  }


}
