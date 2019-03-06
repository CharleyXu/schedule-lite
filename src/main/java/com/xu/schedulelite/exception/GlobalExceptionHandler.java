package com.xu.schedulelite.exception;

import com.xu.schedulelite.common.ResponseMessage;
import java.util.NoSuchElementException;
import javax.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author CharleyXu Created on 2019/1/10.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler({Exception.class})
  public ResponseMessage<String> handler(Exception e) {

    if (e instanceof ConnectionException) {
      log.error("[Zookeeper连接异常] {}", e.getMessage(), e);
      return ResponseMessage.error(ErrorCode.CONNECTION_ERROR);
    }

    if (e instanceof NoSuchElementException) {
      log.error("[没有该节点] {}", e.getMessage(), e);
      return ResponseMessage.error(ErrorCode.N0_SUCH_NODE);
    }

    if (e instanceof DuplicateException) {
      log.error("[节点已存在] {}", e.getMessage(), e);
      return ResponseMessage.error(ErrorCode.DUPLICATE_NODE);
    }

    if (e instanceof ValidationException || e instanceof BindException
        || e instanceof ServletRequestBindingException) {
      log.error("[参数不合法] {}", e.getMessage());
      return ResponseMessage.error(ErrorCode.PARAM_ILLEGAL);
    }

    log.error("[系统异常] {}", e);
    return ResponseMessage.error(ErrorCode.SYSTEM_ERROR);
  }

}
