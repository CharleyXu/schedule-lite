package com.xu.schedulelite.util;

import com.google.common.base.Charsets;
import com.xu.schedulelite.exception.ConnectionException;
import com.xu.schedulelite.exception.DuplicateException;
import com.xu.schedulelite.watcher.MyCuratorWatcher;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author CharleyXu Created on 2019/3/5.
 *
 * Zk的简单使用
 */
@Component
@Slf4j
public class ZookeeperUtil {

  @Autowired
  private CuratorFramework curatorFramework;

  /**
   * 创建数据节点，指定创建模式，附带初始化内容
   *
   * @param type 0 PERSISTENT  1 EPHEMERAL 2 PERSISTENT_SEQUENTIAL 持久化并且带序列号 3 EPHEMERAL_SEQUENTIAL
   */
  public void createNode(int type, String path, String content) {
    if (checkExists(path)) {
      throw new DuplicateException();
    }
    try {
      CreateMode createMode = CreateMode.fromFlag(type);
      if (Optional.ofNullable(content).isPresent()) {
        //使用creatingParentContainersIfNeeded(),Curator能够自动递归创建所有所需的父节点
        curatorFramework.create().creatingParentContainersIfNeeded().withMode(createMode)
            .forPath(path, content.getBytes(Charsets.UTF_8));
      } else {
        curatorFramework.create().creatingParentContainersIfNeeded().withMode(createMode)
            .forPath(path);
      }
    } catch (Exception e) {
      throw new ConnectionException(e);
    }
  }

  public void deleteNode(String path) {
    if (!checkExists(path)) {
      throw new NoSuchElementException();
    }
    //删除一个节点，并且递归删除其所有的子节点    guaranteed  withVersion
    try {
      curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @param isGetStat 是否获取该节点的状态信息 stat
   */
  public String readNode(String path, boolean isGetStat) {
    byte[] paths;
    if (!checkExists(path)) {
      throw new NoSuchElementException();
    }
    try {
      if (isGetStat) {
        Stat stat = new Stat();
        paths = curatorFramework.getData().storingStatIn(stat).forPath(path);
        log.info(stat.toString());
      } else {
        paths = curatorFramework.getData().forPath(path);
      }
      return new String(paths, Charsets.UTF_8);
    } catch (Exception e) {
      throw new ConnectionException(e);
    }
  }

  /**
   * @param forceVersion 强制指定版本
   */
  public Stat updateNode(String path, String content, Integer forceVersion) {
    if (!checkExists(path)) {
      throw new NoSuchElementException();
    }
    try {
      if (Optional.ofNullable(forceVersion).isPresent()) {
        return curatorFramework.setData().withVersion(forceVersion)
            .forPath(path, content.getBytes(Charsets.UTF_8));
      } else {
        return curatorFramework.setData().forPath(path, content.getBytes(Charsets.UTF_8));
      }
    } catch (Exception e) {
      throw new ConnectionException(e);
    }
  }

  public boolean checkExists(String path) {
    try {
      return null != curatorFramework.checkExists().forPath(path);
    } catch (Exception e) {
      throw new ConnectionException(e);
    }
  }

  /**
   * 获取某个节点的所有子节点路径
   */
  public List<String> getChildren(String path) {
    try {
      return curatorFramework.getChildren().forPath(path);
    } catch (Exception e) {
      throw new ConnectionException(e);
    }
  }

  public String getNodeUsingWatcher(String path) {
    byte[] paths;
    if (!checkExists(path)) {
      throw new NoSuchElementException();
    }
    try {
      paths = curatorFramework.getData().usingWatcher(new MyCuratorWatcher()).forPath(path);
      return new String(paths, Charsets.UTF_8);
    } catch (Exception e) {
      throw new ConnectionException(e);
    }
  }

  /**
   * 节点监听
   *
   * 修改、删除节点会触发事件
   *
   * NodeCache: 缓存节点，并且可以监听数据节点的变更，会触发事件
   */
  public void addWatcherUsingNodeCache(String path) {
    NodeCache nodeCache = new NodeCache(curatorFramework, path, false);
    try {
      nodeCache.start(true);
      nodeCache.getListenable().addListener(() -> {
        if (Optional.ofNullable(nodeCache.getCurrentData()).isPresent()) {
          log.info(
              "nodeChanged:" + nodeCache.getCurrentData().getPath() + "\t" + new String(
                  nodeCache.getCurrentData().getData(), Charsets.UTF_8));
        } else {
          log.error("无法获取当前缓存的节点数据，可能该节点已被删除");
        }
      });
    } catch (Exception e) {
      throw new ConnectionException(e);
    }
  }

  /**
   * 子节点监听
   *
   * 新增、变更、删除子节点数据
   *
   * 子节点列表并没有被获取
   */
  public void addWatcherUsingPathChildrenCache(String path) {
    PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, path, true);
    try {
      // POST_INITIALIZED_EVENT：异步初始化，初始化之后会触发事件  ，常用
      // NORMAL：异步初始化
      // BUILD_INITIAL_CACHE：同步初始化
      pathChildrenCache.start(StartMode.POST_INITIALIZED_EVENT);
    } catch (Exception e) {
      throw new ConnectionException(e);
    }
    pathChildrenCache.getListenable()
        .addListener((client, event) -> {
          // 通过判断event type的方式来实现不同事件的触发
          // 子节点初始化时触发
          if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
            log.info("子节点初始化成功");
          } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
            log.info(
                "添加子节点：" + event.getData().getPath() + "\t" + new String(event.getData().getData(),
                    Charsets.UTF_8));
          } else if (event.getType()
              .equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
            log.info("删除子节点：" + event.getData().getPath());
          } else if (event.getType()
              .equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
            log.info(
                "更新子节点：" + event.getData().getPath() + "\t" + new String(event.getData().getData(),
                    Charsets.UTF_8));
          }
        });
  }

  /**
   * 当前节点及子节点进行监听，当数据变动时就会响应
   */
  public void addWatcherUsingTreeCache(String path) {
    TreeCache treeCache = new TreeCache(curatorFramework, path);
    try {
      treeCache.start();
    } catch (Exception e) {
      throw new ConnectionException(e);
    }
    treeCache.getListenable().addListener((client, event) -> {
      //event.getType().equals(Type.CONNECTION_SUSPENDED
      log.info(event.getData() == null ? ""
          : "监听到事件:" + event.getType() + "\t" + event.getData().getPath());
    });
  }

}
