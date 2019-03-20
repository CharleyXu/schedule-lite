package com.xu.schedulelite.lock;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

/**
 * @author CharleyXu Created on 2019/3/7.
 */
@ConditionalOnExpression("'${lock.enable}' == 'true'")
@Service
@Slf4j
public class LockService {

  @Autowired
  private CuratorFramework curatorFramework;

  @Value("${lock.path}")
  private String lockPath;

  /**
   * 可重入锁
   */
  public void acquireLock(String clientId) {
    InterProcessMutex interProcessMutex = new InterProcessMutex(curatorFramework, lockPath);
    try {
      interProcessMutex.acquire();
      //模拟操作
      TimeUnit.SECONDS.sleep(5);
      log.info("client:{},currentThreadName:{}", clientId, Thread.currentThread().getName());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        interProcessMutex.release();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

}
