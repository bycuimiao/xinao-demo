/*
 * Created by cuimiao on 2018/3/24.
 */

package com.xinao.shiro.redis;

import redis.clients.jedis.Jedis;

public class MyJedisPool {

  private static redis.clients.jedis.JedisPool jedisPool = null;

  //初始化redis连接池
  static{
    jedisPool = new redis.clients.jedis.JedisPool("127.0.0.1",6379);
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
      jedis.close();
    }
  }




  public static void main(String[] args){
    try {
      Jedis jedis = MyJedisPool.getJedis();
      Jedis jedis2 = MyJedisPool.getJedis();
      Jedis jedis3 = MyJedisPool.getJedis();
      Jedis jedis4 = MyJedisPool.getJedis();
      Jedis jedis5 = MyJedisPool.getJedis();
      Jedis jedis6 = MyJedisPool.getJedis();
      Jedis jedis7 = MyJedisPool.getJedis();
      Jedis jedis8 = MyJedisPool.getJedis();
      //Jedis jedis9 = MyJedisPool.getJedis();
      jedis.set("test", "hello MyJedisPool.");
      jedis.set("hello", "MyJedisPool.");
      System.out.println(jedis.get("test"));
      //MyJedisPool.returnResource(jedis);
      jedis.close();
      //Thread.sleep(10000L);
      //测试发现释放Jedis资源后，下面的这个还能返回JedisPool  ????
      System.out.println(jedis.get("hello"));
      Jedis jedis10 = MyJedisPool.getJedis();
      System.out.println(jedis10.get("hello") + "10");
      System.out.println(jedis.get("hello") + "01");
      System.out.println("fuck");
    }catch (Exception e){

    }

  }
}