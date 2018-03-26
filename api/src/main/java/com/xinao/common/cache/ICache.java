/*
 * Created by guanshang on 2016-11-08.
 */

package com.xinao.common.cache;

import java.util.List;
import java.util.Set;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-08
 */
public interface ICache {
  /**
   * 删除key.
   *
   * @param key key
   * @return 是否成功
   */
  Object del(byte[] key);

  /**
   * 获取key.
   *
   * @param key key
   * @return value
   */
  byte[] get(byte[] key);

  /**
   * 简单的Get.
   *
   * @param key          key
   * @param requiredType value的泛型类型
   * @param <T>          泛型类型
   * @return 返回泛型类
   */
  @SuppressWarnings({"unchecked", "varargs"})
  <T> T get(String key, Class<T>... requiredType);

  /**
   * 简单的set.
   *
   * @param key   key
   * @param value 存储的值
   * @return 是否成功
   */
  Object set(String key, Object value);

  /**
   * set过期时间的.
   *
   * @param key        key
   * @param value      存储的值
   * @param expireTime 过期时间（秒）
   * @return 是否成功
   */
  Object setex(String key, Object value, int expireTime);

  /**
   * mapKey get .
   *
   * @param mapkey       mapKey
   * @param key          map里的key
   * @param requiredType value的泛型类型
   * @param <T>          泛型类型
   * @return 返回泛型类
   */
  <T> T getVByMap(String mapkey, String key, Class<T> requiredType);

  /**
   * mapKey set.
   *
   * @param mapkey map
   * @param key    map里的key
   * @param value  map里的value
   * @return 是否成功
   */
  Object setVByMap(String mapkey, String key, Object value);

  /**
   * 删除Map里的值.
   *
   * @param mapKey map
   * @param dkey   map里的key
   * @return 是否成功
   */
  Object delByMapKey(String mapKey, String... dkey);

  /**
   * Set get.
   *
   * @param setKey       set
   * @param requiredType value的泛型类型
   * @param <T>          泛型类型
   * @return 返回泛型类
   */
  <T> Set<T> getVBySet(String setKey, Class<T> requiredType);

  /**
   * 获取Set长度.
   *
   * @param setKey set
   * @return Set长度
   */
  Long getLenBySet(String setKey);

  /**
   * 删除Set.
   *
   * @param setKey set
   * @return 是否成功
   */
  Long delSetByKey(String setKey);

  /**
   * 随机Set中的一个值.
   *
   * @param setKey       set
   * @param requiredType value的泛型类型
   * @param <T>          泛型类型
   * @return 返回泛型类
   */
  <T> T srandmember(String setKey, Class<T> requiredType);

  /**
   * Set set.
   *
   * @param setKey set
   * @param value  value值
   * @return 是否成功
   */
  Object setVBySet(String setKey, Object value);

  /**
   * List set.
   *
   * @param listKey list
   * @param value   value值
   * @return 是否成功
   */
  Object setVByList(String listKey, Object value);

  /**
   * list get.
   *
   * @param listKey      list
   * @param start        开始位置
   * @param end          结束位置
   * @param requiredType value的泛型类型
   * @param <T>          泛型类型
   * @return 返回泛型类
   */
  <T> List<T> getVByList(String listKey, int start, int end, Class<T> requiredType);

  /**
   * 获取list长度.
   *
   * @param listKey list
   * @return 长度
   */
  Long getLenByList(String listKey);

  /**
   * 删除list.
   *
   * @param listKey list
   * @return 是否成功
   */
  Long delListByKey(String listKey);

  /**
   * 删除.
   *
   * @param dkey key可变参数
   * @return 是否成功
   */
  Long delByKey(String... dkey);

  /**
   * 判断是否存在.
   *
   * @param existskey key
   * @return 是否存在
   */
  boolean exists(String existskey);


  /**
   * 匹配相关缓存.
   *
   * @param pattern 匹配规则
   * @return 匹配key列表
   */
  Set<String> getPatternKeys(String pattern);

  /**
   * redis自增.
   *
   * @param key 匹配规则
   * @return 自增之后的结果
   */
  Long incr(String key);

  /**
   * redisz自减.
   *
   * @param key 匹配规则
   * @return 自减之后的结果
   */
  Long decr(String key);
}
