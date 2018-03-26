/*
 * Created by guanshang on 2016-11-09.
 */

package com.xinao.shiro.listenter;

import com.xinao.shiro.session.IShiroSessionRepository;
import com.xinao.shiro.util.LoggerUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-11-09
 */
public class CustomSessionListener implements SessionListener {

  private IShiroSessionRepository shiroSessionRepository;

  /**
   * 一个回话的生命周期开始.
   */
  @Override
  public void onStart(Session session) {
    LoggerUtils.debug(this.getClass(), "on start");
  }

  /**
   * 一个回话的生命周期结束.
   */
  @Override
  public void onStop(Session session) {
    LoggerUtils.debug(this.getClass(), "on stop");
  }

  @Override
  public void onExpiration(Session session) {
    shiroSessionRepository.deleteSession(session.getId());
  }

  public IShiroSessionRepository getShiroSessionRepository() {
    return shiroSessionRepository;
  }

  public void setShiroSessionRepository(
      IShiroSessionRepository shiroSessionRepository) {
    this.shiroSessionRepository = shiroSessionRepository;
  }


}
