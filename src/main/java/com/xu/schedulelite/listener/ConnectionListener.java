package com.xu.schedulelite.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author CharleyXu Created on 2019/3/20.
 */
@Slf4j
public class ConnectionListener {

  @Autowired
  private CuratorFramework curatorFramework;

  public void addListener() {
    //CONNECTED 已连接  RECONNECTED 重新连接  LOST 确认丢失  SUSPENDED 暂停挂起
    curatorFramework.getConnectionStateListenable()
        .addListener((curatorFramework, connectionState) -> {
          if (connectionState == ConnectionState.CONNECTED) {
            log.warn("connected established");
          } else if (connectionState == ConnectionState.LOST) {
            log.error("connection lost : " + connectionState.name());
          } else {
            log.warn("connection state : " + connectionState.name());
          }
        });
  }

}
