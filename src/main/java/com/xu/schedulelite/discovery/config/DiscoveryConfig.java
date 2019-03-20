package com.xu.schedulelite.discovery.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author CharleyXu Created on 2019/3/6.
 */
@Configuration
@ConditionalOnExpression("'${discovery.enable}' == 'true'")
public class DiscoveryConfig {

  @Bean(initMethod = "init")
  public ServiceRegistryConfig initRegistryConfig() {
    return new ServiceRegistryConfig();
  }

  @Bean(initMethod = "addWatcher")
  public ServiceDiscoveryConfig initDiscoveryConfig() {
    return new ServiceDiscoveryConfig();
  }
}
