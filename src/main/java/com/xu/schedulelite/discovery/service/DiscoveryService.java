package com.xu.schedulelite.discovery.service;

import com.xu.schedulelite.discovery.config.ServiceDiscoveryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

/**
 * @author CharleyXu Created on 2019/3/8.
 *
 * Service registration and discovery
 *
 * 服务注册与发现
 */
@Service
@ConditionalOnExpression("'${discovery.enable}' == 'true'")
public class DiscoveryService {

  @Autowired
  private ServiceDiscoveryConfig serviceDiscoveryConfig;

  @Value("${discovery.serviceName}")
  private String serviceName;

  public Object getNodes() {
    try {
      // Collection<ServiceInstance<ServiceDetail>> serviceInstances
      return serviceDiscoveryConfig
          .getServiceDiscovery().queryForInstances(serviceName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }


}
