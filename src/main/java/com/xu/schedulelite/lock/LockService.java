package com.xu.schedulelite.lock;

import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author CharleyXu Created on 2019/3/7.
 */
@Service
public class LockService {

  @Autowired
  private CuratorFramework curatorFramework;

  public void requireLock() {

  }

}
