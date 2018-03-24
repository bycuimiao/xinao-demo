/*
 * Created by cuimiao on 2018/3/24.
 */

package com.xinao.demo;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class UseJedisPool {

  private static JedisPool jedisPool = null;

  //初始化redis连接池
  static{
    jedisPool = new JedisPool("127.0.0.1",6379);
  }

  //获取Jedis实例
  public synchronized static Jedis getJedis(){
    if(jedisPool != null){
      Jedis resource = jedisPool.getResource();
      return resource;
    }else{
      return null;
    }
  }

  //释放Jedis资源
  public static void returnResource(final Jedis jedis){
    if(jedis != null){
      jedisPool.returnResource(jedis);
    }
  }




  public static void main(String[] args){
    try {
      Jedis jedis = UseJedisPool.getJedis();
      Jedis jedis2 = UseJedisPool.getJedis();
      Jedis jedis3 = UseJedisPool.getJedis();
      Jedis jedis4 = UseJedisPool.getJedis();
      Jedis jedis5 = UseJedisPool.getJedis();
      Jedis jedis6 = UseJedisPool.getJedis();
      Jedis jedis7 = UseJedisPool.getJedis();
      Jedis jedis8 = UseJedisPool.getJedis();
      //Jedis jedis9 = UseJedisPool.getJedis();
      jedis.set("test", "hello JedisPool.");
      jedis.set("hello", "JedisPool.");
      System.out.println(jedis.get("test"));
      //UseJedisPool.returnResource(jedis);
      jedis.close();
      //Thread.sleep(10000L);
      //测试发现释放Jedis资源后，下面的这个还能返回JedisPool  ????
      System.out.println(jedis.get("hello"));
      Jedis jedis10 = UseJedisPool.getJedis();
      System.out.println(jedis10.get("hello") + "10");
      System.out.println(jedis.get("hello") + "01");
      System.out.println("fuck");
    }catch (Exception e){

    }

  }
}