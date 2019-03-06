package com.xu.schedulelite.config;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;
import java.util.List;
import java.util.Optional;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author CharleyXu Created on 2019/3/5.
 */
@Configuration
public class CuratorConfiguration {

  @Value("${curator.maxRetries}")
  private int maxRetries;

  @Value("${curator.elapsedTimeMs}")
  private int elapsedTimeMs;

  @Value("${curator.serverLists}")
  private String serverLists;

  @Value("${curator.sessionTimeoutMs}")
  private int sessionTimeoutMs;

  @Value("${curator.connectionTimeoutMs}")
  private int connectionTimeoutMs;

  @Value("${curator.digest}")
  private String digest;

  @Value("${curator.namespace}")
  private String namespace;

  @Bean(initMethod = "start")
  public CuratorFramework curatorFramework() {

    CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
        .connectString(serverLists)
        .retryPolicy(new ExponentialBackoffRetry(elapsedTimeMs, maxRetries))
        .sessionTimeoutMs(sessionTimeoutMs)
        .connectionTimeoutMs(connectionTimeoutMs);
    //设置命令空间,分离业务
    if (Optional.ofNullable(namespace).isPresent()) {
      builder.namespace(namespace);
    }

    if (!Strings.isNullOrEmpty(digest)) {
      builder.authorization("digest", digest.getBytes(Charsets.UTF_8))
          .aclProvider(new ACLProvider() {
            @Override
            public List<ACL> getDefaultAcl() {
              return ZooDefs.Ids.CREATOR_ALL_ACL;
            }

            @Override
            public List<ACL> getAclForPath(final String path) {
              return ZooDefs.Ids.CREATOR_ALL_ACL;
            }
          });
    }
    return builder.build();
  }
}
