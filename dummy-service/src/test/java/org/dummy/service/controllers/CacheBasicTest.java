package org.dummy.service.controllers;

import org.dummy.service.cache.HotRodCache;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CacheBasicTest {
  @Autowired
  protected HotRodCache hotRodCache;

  @RegisterExtension // test infinispan server
  static InfinispanServerExtension server = new InfinispanServerExtension("gatewaySessions", "localhost", 11222, true, true, false, false);

  @BeforeEach
  public void overrideRemoteCacheManager() {
    RemoteCacheManager remoteCacheManager = server.hotRodClient();
    hotRodCache.setRemoteCacheManager(remoteCacheManager);
    getCache().clear();
  }
  protected RemoteCache<String, Object> getCache(){
    return hotRodCache.getCache();
  }
}
