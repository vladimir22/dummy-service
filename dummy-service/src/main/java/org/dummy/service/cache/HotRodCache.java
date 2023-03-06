package org.dummy.service.cache;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HotRodCache {

  @Setter
  private RemoteCacheManager remoteCacheManager;
  private RemoteCache<String, Object> cache;

  @Value("${controller.hotrod.cache.name}")
  @Getter
  private String cacheName;


  public RemoteCache<String, Object> getCache() {
    if ( cache == null ) {

      remoteCacheManager = new RemoteCacheManager();
      cache = remoteCacheManager.getCache(cacheName);
    }
    return cache;
  }

  public void put(String key, String value){
    getCache().put(key, value);
  }

  public Object get(String key){
    Object value = getCache().get(key);
    return value;
  }

  public void delete(String key){
    getCache().remove(key);
  }

}
