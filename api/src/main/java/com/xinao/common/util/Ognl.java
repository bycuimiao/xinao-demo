/*
 * Created by houyefeng on 2016-10-19.
 */

package com.xinao.common.util;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;


/**
 * MyBaits中使用的ognl表达式.
 *
 * @author houyefeng
 * @version 0.0.1
 * @since 0.0.1 2016-10-19
 */
public class Ognl {
  /**
   * 判断输入对象是否为空.
   *
   * <p>
   * 只支持对{@code String，Collection，Array，Map}的判断。
   * </p>
   *
   * @param obj 要判断的对象
   * @return 为空返回true，否则返回false；不支持的类型也返回false
   */
  public static boolean isEmpty(Object obj) {
    if (obj == null) {
      return true;
    } else if (obj instanceof String) {
      return StringUtils.isEmpty((String) obj);
    } else if (obj instanceof Collection) {
      return ((Collection) obj).isEmpty();
    } else if (obj.getClass().isArray()) {
      return Array.getLength(obj) == 0;
    } else if (obj instanceof Map) {
      return ((Map) obj).isEmpty();
    }

    return false;
  }

  /**
   * 判断输入对象是否不为空.
   *
   * <p>
   * 只支持对{@code String，Collection，Array，Map}的判断。
   * </p>
   *
   * @param obj 要判断的对象
   * @return 不为空或是不支持的类型返回true，否则返回false
   */
  public static boolean isNotEmpty(Object obj) {
    return !isEmpty(obj);
  }
}
