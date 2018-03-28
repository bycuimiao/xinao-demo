/*
 * Created by houyefeng on 2016-10-19.
 */

package com.xinao.common.enums;

/**
 * 枚举类型接口，用于MyBaits中将某个字段的值转成相应的枚举类型和在保存数据时将枚举类型转换成字段值.
 *
 * @author houyefeng
 * @version 0.0.1
 * @since 0.0.1 2016-10-19
 */
public interface Enum<T> {
  public T getValue();
}
