package com.xu.schedulelite.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
  SUCCESS(1, "Success"),
  SYSTEM_ERROR(0, "系统内部异常"),
  PARAM_ILLEGAL(0, "参数不合法"),
  CONNECTION_ERROR(0, "Zookeeper连接异常"),
  N0_SUCH_NODE(0, "没有该节点"),
  DUPLICATE_NODE(0, "节点已存在");

  private Integer code;
  private String msg;

  ErrorCode(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

}
