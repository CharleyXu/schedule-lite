package com.xu.schedulelite.discovery.config;

import com.xu.schedulelite.discovery.domain.ServiceDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author CharleyXu Created on 2019/3/19.
 *
 * 服务注册
 */
@Slf4j
public class ServiceRegistryConfig {

  @Value("${discovery.root}")
  private String registerRootPath;
  @Value("${discovery.serviceName}")
  private String serviceName;
  @Value("${discovery.desc}")
  private String desc;
  @Value("${discovery.weight}")
  private int weight;

  @Autowired
  private CuratorFramework curatorFramework;

  public void init() {
    /**
     * 指定服务的 地址，端口，名称
     */
    ServiceInstanceBuilder<ServiceDetail> sib;
    try {
      sib = ServiceInstance.builder();
      sib.address("192.0.0.1");
      sib.port(8001);
      sib.name(serviceName);
      sib.payload(new ServiceDetail(desc, weight));

      ServiceInstance<ServiceDetail> instance = sib.build();

      ServiceDiscovery<ServiceDetail> serviceDiscovery = ServiceDiscoveryBuilder
          .builder(ServiceDetail.class)
          .client(curatorFramework)
          .basePath(registerRootPath)
          .build();
      //服务注册
      serviceDiscovery.registerService(instance);
      serviceDiscovery.start();
      log.warn(desc + " 节点服务注册完成");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }

  }

}
