package com.xu.schedulelite.discovery.controller;

import com.xu.schedulelite.common.ResponseMessage;
import com.xu.schedulelite.discovery.service.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CharleyXu Created on 2019/3/8.
 *
 * 服务发现
 *
 */
@ConditionalOnExpression("'${discovery.enable}' == 'true'")
@RestController
public class DiscoveryController {

  @Autowired
  private DiscoveryService discoveryService;

  @GetMapping(value = "/discovery/findAllNodes")
  public ResponseMessage findAllNodes() {
    return ResponseMessage.success(discoveryService.getNodes());
  }
}
