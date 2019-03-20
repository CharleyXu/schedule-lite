package com.xu.schedulelite.discovery.config;

import com.xu.schedulelite.discovery.domain.ServiceDetail;
import com.xu.schedulelite.util.ZookeeperUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author CharleyXu Created on 2019/3/19.
 *
 * 服务发现
 */
@Slf4j
public class ServiceDiscoveryConfig {

  @Value("${discovery.path}")
  private String registerRootPath;
  @Autowired
  private CuratorFramework curatorFramework;
  @Autowired
  private ZookeeperUtil zookeeperUtil;

  public ServiceDiscovery<ServiceDetail> getServiceDiscovery() {
    ServiceDiscovery<ServiceDetail> serviceDiscovery = ServiceDiscoveryBuilder
        .builder(ServiceDetail.class)
        .client(curatorFramework)
        .basePath(registerRootPath)
        .build();
    return serviceDiscovery;
  }

  /**
   * 添加节点监听
   */
  public void addWatcher() {
    zookeeperUtil.addWatcherUsingTreeCache(registerRootPath);
    log.warn("成功添加" + registerRootPath + "节点监听");
  }

}
