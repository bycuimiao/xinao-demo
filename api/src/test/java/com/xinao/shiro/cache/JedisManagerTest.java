package com.xinao.shiro.cache;

import com.xinao.base.BaseTest;
import com.xinao.common.util.SerializeUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @auther cuimiao
 * @date 2018/3/14/014  23:08
 * @Description: ${todo}
 */
public class JedisManagerTest extends BaseTest {

    @Autowired
    private JedisManager jedisManager;

    @Autowired
    private JedisShiroSessionRepository jedisShiroSessionRepository;

    @Test
    public void getJedis() throws Exception {
    }

    @Test
    public void returnResource() throws Exception {
    }

    @Test
    public void getValueByKey() throws Exception {
        int dbindex = 0;
        byte[] key = SerializeUtil.serialize("abc");
        System.out.print(SerializeUtil.deserialize(jedisManager.getValueByKey(dbindex,key)));
    }

    @Test
    public void deleteByKey() throws Exception {
    }

    @Test
    public void saveValueByKey() throws Exception {
        int dbindex = 0;
        byte[] key = SerializeUtil.serialize("abcd");
        byte[] value = SerializeUtil.serialize("1234");
        int expireTime = 18000;
        try {
            jedisShiroSessionRepository.getJedisManager().saveValueByKey(dbindex, key, value, expireTime);
        } catch (Exception e) {
            System.out.println("error - " + e);
        }
        System.out.println("end");
    }

    @Test
    public void getJedisPool() throws Exception {
    }

    @Test
    public void setJedisPool() throws Exception {
    }

    @Test
    public void allSession() throws Exception {
    }

}