/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.util;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class Ini4j {
  /**
   * 用linked hash map 来保持有序的读取.
   */
  final LinkedHashMap<String, LinkedHashMap<String, String>> coreMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
  /**
   * 当前Section的引用.
   */
  String currentSection = null;

  /**
   * 读取.
   *
   * @param file 文件
   * @throws FileNotFoundException FileNotFoundException
   */
  public Ini4j(File file) throws FileNotFoundException {
    this.init(new BufferedReader(new FileReader(file)));
  }

  /**
   * 重载读取.
   *
   * @param path 给文件路径
   * @throws FileNotFoundException FileNotFoundException
   */
  public Ini4j(String path) throws FileNotFoundException {
    this.init(new BufferedReader(new FileReader(path)));
  }

  /**
   * 重载读取.
   *
   * @param source ClassPathResource 文件，如果文件在resource 里，那么直接 new ClassPathResource("file name");
   * @throws IOException IOException
   */
  public Ini4j(ClassPathResource source) throws IOException {
    this(source.getFile());
  }

  void init(BufferedReader bufferedReader) {
    try {
      read(bufferedReader);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("IO Exception:" + e);
    }
  }

  /**
   * 读取文件.
   *
   * @param reader reader
   * @throws IOException IOException
   */
  void read(BufferedReader reader) throws IOException {
    String line = null;
    while ((line = reader.readLine()) != null) {
      parseLine(line);
    }
  }

  /**
   * 转换.
   *
   * @param line line
   */
  void parseLine(String line) {
    line = line.trim().replaceAll("\\s*", "");
    // 此部分为注释
    if (line.matches("^\\#.*$")) {
      return;
    } else if (line.matches("^\\[\\S+\\]$")) {
      // section
      String section = line.replaceFirst("^\\[(\\S+)\\]$", "$1");
      addSection(section);
    } else if (line.matches("^\\S+=.*$")) {
      // key ,value
      int indexOf = line.indexOf("=");
      String key = line.substring(0, indexOf).trim();
      String value = line.substring(indexOf + 1).trim();
      addKeyValue(currentSection, key, value);
    }
  }


  /**
   * 增加新的Key和Value.
   *
   * @param currentSection currentSection
   * @param key            key
   * @param value          value
   */
  void addKeyValue(String currentSection, String key, String value) {
    if (!coreMap.containsKey(currentSection)) {
      return;
    }
    Map<String, String> childMap = coreMap.get(currentSection);
    childMap.put(key, value);
  }


  /**
   * 增加Section.
   *
   * @param section section
   */
  void addSection(String section) {
    if (!coreMap.containsKey(section)) {
      currentSection = section;
      LinkedHashMap<String, String> childMap = new LinkedHashMap<String, String>();
      coreMap.put(section, childMap);
    }
  }

  /**
   * 获取配置文件指定Section和指定子键的值.
   *
   * @param section section
   * @param key     key
   * @return value
   */
  public String get(String section, String key) {
    if (coreMap.containsKey(section)) {
      return get(section).containsKey(key) ? get(section).get(key) : null;
    }
    return null;
  }


  /**
   * 获取配置文件指定Section的子键和值.
   *
   * @param section section
   * @return values
   */
  public Map<String, String> get(String section) {
    return coreMap.containsKey(section) ? coreMap.get(section) : null;
  }

  /**
   * 获取这个配置文件的节点和值.
   *
   * @return 配置文件的节点和值
   */
  public LinkedHashMap<String, LinkedHashMap<String, String>> get() {
    return coreMap;
  }

}
