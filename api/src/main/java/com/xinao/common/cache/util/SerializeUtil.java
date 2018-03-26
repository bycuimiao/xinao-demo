/*
 * Created by guanshang on 2016-11-08.
 */

package com.xinao.common.cache.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-08
 */
public class SerializeUtil {
  protected static Logger logger = LoggerFactory.getLogger(SerializeUtil.class);

  /**
   * 序列化.
   *
   * @param value value值
   * @return 序列化结果
   */
  public static byte[] serialize(Object value) {
    if (value == null) {
      throw new NullPointerException("Can't serialize null");
    }
    byte[] rv = null;
    ByteArrayOutputStream bos = null;
    ObjectOutputStream os = null;
    try {
      bos = new ByteArrayOutputStream();
      os = new ObjectOutputStream(bos);
      os.writeObject(value);
      os.close();
      bos.close();
      rv = bos.toByteArray();
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("serialize error", e);
      }
    } finally {
      close(os);
      close(bos);
    }
    return rv;
  }

  /**
   * 反序列化.
   *
   * @param in 输入
   * @return 反序列化结果
   */
  public static Object deserialize(byte[] in) {
    return deserialize(in, Object.class);
  }

  /**
   * 反序列化.
   *
   * @param in           输入
   * @param requiredType value泛型
   * @param <T>          泛型类型
   * @return 反序列化结果
   */
  @SuppressWarnings({"unchecked", "varargs"})
  public static <T> T deserialize(byte[] in, Class<T>... requiredType) {
    Object rv = null;
    ByteArrayInputStream bis = null;
    ObjectInputStream is = null;
    try {
      if (in != null) {
        bis = new ByteArrayInputStream(in);
        is = new ObjectInputStream(bis);
        rv = is.readObject();
      }
    } catch (Exception e) {
      if (logger.isErrorEnabled()) {
        logger.error("deserialize error", e);
      }
    } finally {
      close(is);
      close(bis);
    }
    return (T) rv;
  }

  /**
   * 关闭连接.
   *
   * @param closeable closeable
   */
  private static void close(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException e) {
        if (logger.isErrorEnabled()) {
          logger.error("close stream error", e);
        }
      }
    }
  }
}
