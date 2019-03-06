package com.xu.schedulelite.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @author CharleyXu Created on 2019/3/6.
 *
 * 监听只会触发一次，监听完毕后就会销毁，一次性
 */
@Slf4j
public class MyNativeWatcher implements Watcher {

  @Override
  public void process(WatchedEvent watchedEvent) {
    log.info("触发NativeWatcher，节点路径:" + watchedEvent.getPath());
  }
}
