package com.xu.schedulelite.watcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.zookeeper.WatchedEvent;

/**
 * @author CharleyXu Created on 2019/3/6.
 *
 * 监听只会触发一次，监听完毕后就会销毁，一次性
 */
@Slf4j
public class MyCuratorWatcher implements CuratorWatcher {

  @Override
  public void process(WatchedEvent watchedEvent) {
    log.info("触发CuratorWatcher，节点路径:" + watchedEvent.getPath());

  }
}
