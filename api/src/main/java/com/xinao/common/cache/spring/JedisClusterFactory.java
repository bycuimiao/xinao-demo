/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.common.cache.spring;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {

  private String addressConfig;

  private JedisCluster jedisCluster;
  private Integer timeout;
  private Integer maxRedirections;
  private GenericObjectPoolConfig genericObjectPoolConfig;

  private Pattern pattern = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

  @Override
  public JedisCluster getObject() throws Exception {
    return jedisCluster;
  }

  @Override
  public Class<? extends JedisCluster> getObjectType() {
    return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);
  }

  @Override
  public boolean isSingleton() {
    return true;
  }


  private Set<HostAndPort> parseHostAndPort() throws Exception {
    try {
      String[] addressArr = this.addressConfig.split(",");

      Set<HostAndPort> haps = new HashSet<>();
      for (String address : addressArr) {
        boolean isIpPort = pattern.matcher(address).matches();
        if (!isIpPort) {
          throw new IllegalArgumentException("ip or port  illegitimate");
        }

        String[] ipAndPort = address.split(":");
        HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
        haps.add(hap);
      }

      return haps;
    } catch (IllegalArgumentException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new Exception("analyze jedis file fail", ex);
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    Set<HostAndPort> haps = this.parseHostAndPort();
    jedisCluster = new JedisCluster(haps, timeout, maxRedirections, genericObjectPoolConfig);

  }

  public void setAddressConfig(String addressConfig) {
    this.addressConfig = addressConfig;
  }

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  public void setMaxRedirections(int maxRedirections) {
    this.maxRedirections = maxRedirections;
  }


  public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
    this.genericObjectPoolConfig = genericObjectPoolConfig;
  }
}
