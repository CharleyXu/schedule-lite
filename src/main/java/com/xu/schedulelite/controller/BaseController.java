package com.xu.schedulelite.controller;

import com.xu.schedulelite.common.ResponseMessage;
import com.xu.schedulelite.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author CharleyXu Created on 2019/3/5.
 */
@RestController
@RequestMapping(value = "/base")
public class BaseController {

  @Autowired
  private BaseService baseService;

  @PostMapping(value = "/createNode")
  public ResponseMessage createNode(int type, String path,
      @RequestParam(required = false) String content) {
    baseService.createNode(type, path, content);
    return ResponseMessage.success();
  }

  @DeleteMapping(value = "/deleteNode")
  public ResponseMessage deleteNode(String path) {
    baseService.deleteNode(path);
    return ResponseMessage.success();
  }

  @GetMapping(value = "/readNode")
  public ResponseMessage readNode(String path, boolean isGetStat) {
    return ResponseMessage.success(baseService.readNode(path, isGetStat));
  }

  @PutMapping(value = "/updateNode")
  public ResponseMessage updateNode(String path, String content,
      @RequestParam(required = false) Integer forceVersion) {
    return ResponseMessage.success(baseService.updateNode(path, content, forceVersion));
  }

  @GetMapping(value = "/checkExists")
  public ResponseMessage checkExists(String path) {
    return ResponseMessage.success(baseService.checkExists(path));
  }

  @GetMapping(value = "/getChildren")
  public ResponseMessage getChildren(String path) {
    return ResponseMessage.success(baseService.getChildren(path));
  }

  @GetMapping(value = "/getNodeUsingWatcher")
  public ResponseMessage getNodeByWatcher(String path) {
    return ResponseMessage.success(baseService.getNodeUsingWatcher(path));
  }

  @PostMapping(value = "/addWatcherUsingNodeCache")
  public ResponseMessage addWatcherUsingNodeCache(String path) {
    baseService.addWatcherUsingNodeCache(path);
    return ResponseMessage.success();
  }

  @PostMapping(value = "/addWatcherUsingPathChildrenCache")
  public ResponseMessage addWatcherUsingPathChildrenCache(String path) {
    baseService.addWatcherUsingPathChildrenCache(path);
    return ResponseMessage.success();
  }

  @PostMapping(value = "/addWatcherUsingTreeCache")
  public ResponseMessage addWatcherUsingTreeCache(String path) {
    baseService.addWatcherUsingTreeCache(path);
    return ResponseMessage.success();
  }

}
