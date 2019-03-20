package com.xu.schedulelite.lock;

import com.xu.schedulelite.common.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CharleyXu Created on 2019/3/20.
 */
@ConditionalOnExpression("'${lock.enable}' == 'true'")
@RestController
public class LockController {

  @Autowired
  private LockService lockService;

  @GetMapping("/lock/acquireLock")
  public ResponseMessage acquireLock(String clientId) {
    lockService.acquireLock(clientId);
    return ResponseMessage.success();
  }

}
