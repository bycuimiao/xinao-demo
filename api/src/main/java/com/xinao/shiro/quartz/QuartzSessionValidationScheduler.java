/*
 * Created by guanshang on 2016-12-12.
 */

package com.xinao.shiro.quartz;

import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionValidationScheduler;
import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author guanshang
 * @version 0.0.1
 * @since 0.0.1 2016-12-12
 */
public class QuartzSessionValidationScheduler implements SessionValidationScheduler {
  private final Logger log = LoggerFactory.getLogger(org.apache.shiro.session.mgt.quartz.QuartzSessionValidationScheduler.class);


  /**
   * The default interval at which sessions will be validated (1 hour).
   * This can be overridden by calling {@link #setSessionValidationInterval(long)}
   */
  public static final long DEFAULT_SESSION_VALIDATION_INTERVAL = DefaultSessionManager.DEFAULT_SESSION_VALIDATION_INTERVAL;

  /**
   * The name assigned to the quartz job.
   */
  private static final String JOB_NAME = "SessionValidationJob";


  /**
   * The configured Quartz scheduler to use to schedule the Quartz job.  If no scheduler is
   * configured, the schedular will be retrieved by calling {@link StdSchedulerFactory#getDefaultScheduler()}.
   */
  private Scheduler scheduler;

  private boolean schedulerImplicitlyCreated = false;

  private boolean enabled = false;

  /**
   * The session manager used to validate sessions.
   */
  private ValidatingSessionManager sessionManager;

  /**
   * The session validation interval in milliseconds.
   */
  private long sessionValidationInterval = DEFAULT_SESSION_VALIDATION_INTERVAL;

  /**
   * Default constructor.
   */
  public QuartzSessionValidationScheduler() {
  }

  /**
   * Constructor that specifies the session manager that should be used for validating sessions.
   *
   * @param sessionManager the <tt>SessionManager</tt> that should be used to validate sessions.
   */
  public QuartzSessionValidationScheduler(ValidatingSessionManager sessionManager) {
    this.sessionManager = sessionManager;
  }


  protected Scheduler getScheduler() throws SchedulerException {
    if (scheduler == null) {
      scheduler = StdSchedulerFactory.getDefaultScheduler();
      schedulerImplicitlyCreated = true;
    }
    return scheduler;
  }

  public void setScheduler(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  public void setSessionManager(ValidatingSessionManager sessionManager) {
    this.sessionManager = sessionManager;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  /**
   * Specifies how frequently (in milliseconds) this Scheduler will call .
   *
   * @param sessionValidationInterval sessionValidationInterval
   */
  public void setSessionValidationInterval(long sessionValidationInterval) {
    this.sessionValidationInterval = sessionValidationInterval;
  }

  /**
   * Starts session validation by creating a Quartz simple trigger.
   */
  public void enableSessionValidation() {

    if (log.isDebugEnabled()) {
      log.debug("Scheduling session validation job using Quartz with session validation interval of [" + sessionValidationInterval + "]ms...");
    }

    try {
      SimpleTrigger trigger = TriggerBuilder.newTrigger().startNow().withIdentity(JOB_NAME, Scheduler.DEFAULT_GROUP)
          .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMilliseconds(sessionValidationInterval))
          .build();

      JobDetail detail = JobBuilder.newJob(QuartzSessionValidationJob.class)
          .withIdentity(JOB_NAME, Scheduler.DEFAULT_GROUP).build();


      detail.getJobDataMap().put(QuartzSessionValidationJob.SESSION_MANAGER_KEY, sessionManager);

      Scheduler scheduler = getScheduler();

      scheduler.scheduleJob(detail, trigger);
      if (schedulerImplicitlyCreated) {
        scheduler.start();
        if (log.isDebugEnabled()) {
          log.debug("Successfully started implicitly created Quartz Scheduler instance.");
        }
      }
      this.enabled = true;

      if (log.isDebugEnabled()) {
        log.debug("Session validation job successfully scheduled with Quartz.");
      }

    } catch (SchedulerException e) {
      if (log.isErrorEnabled()) {
        log.error("Error starting the Quartz session validation job.  Session validation may not occur.", e);
      }
    }
  }

  /**
   * disableSessionValidation.
   */
  public void disableSessionValidation() {
    if (log.isDebugEnabled()) {
      log.debug("Stopping Quartz session validation job...");
    }

    Scheduler scheduler;
    try {
      scheduler = getScheduler();
      if (scheduler == null) {
        if (log.isWarnEnabled()) {
          log.warn("getScheduler() method returned a null Quartz scheduler, which is unexpected.  Please "
              + "check your configuration and/or implementation.  Returning quietly since there is no "
              + "validation job to remove (scheduler does not exist).");
        }
        return;
      }
    } catch (SchedulerException e) {
      if (log.isWarnEnabled()) {
        log.warn("Unable to acquire Quartz Scheduler.  Ignoring and returning (already stopped?)", e);
      }
      return;
    }

    try {
      scheduler.unscheduleJob(new TriggerKey(JOB_NAME, Scheduler.DEFAULT_GROUP));
      if (log.isDebugEnabled()) {
        log.debug("Quartz session validation job stopped successfully.");
      }
    } catch (SchedulerException e) {
      if (log.isDebugEnabled()) {
        log.debug("Could not cleanly remove SessionValidationJob from Quartz scheduler.  "
            + "Ignoring and stopping.", e);
      }
    }

    this.enabled = false;

    if (schedulerImplicitlyCreated) {
      try {
        scheduler.shutdown();
      } catch (SchedulerException e) {
        if (log.isWarnEnabled()) {
          log.warn("Unable to cleanly shutdown implicitly created Quartz Scheduler instance.", e);
        }
      } finally {
        setScheduler(null);
        schedulerImplicitlyCreated = false;
      }
    }
  }
}
