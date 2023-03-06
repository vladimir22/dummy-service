package org.dummy.service.controllers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dummy.service.cache.HotRodCache;
import org.dummy.service.controllers.dto.HotrodResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
public class HotrodController {

  public final HotRodCache cache;

  @GetMapping(value = "${controller.hotrod.url}")
  public ResponseEntity getEntries() {

    HotrodResponse hotrodResponse = new HotrodResponse();

    long beg = System.currentTimeMillis();

    Map<String, Object> entries = new HashMap<>();
    try {
      cache.getCache().entrySet().forEach(e -> entries.put(e.getKey(), e.getValue()));
    } catch (Exception e) {
      log.error("Hotrod get entries ERROR: error='{}', cache='{}'",e.getMessage(), cache.getCacheName());
      hotrodResponse.setErrorMessage(e.getMessage());
    }

    hotrodResponse.setEntries(entries);

    long end = System.currentTimeMillis();

    HttpStatus status = !entries.isEmpty() ? HttpStatus.OK : HttpStatus.NOT_FOUND;

    log.info("Hotrod get entries: cache='{}', duration={}, status={}", cache.getCacheName(), end-beg, status.value());
    return new ResponseEntity(hotrodResponse, status);
  }


  @GetMapping(value = "${controller.hotrod.url}/{key}")
  public ResponseEntity get(@PathVariable String key) {
    long beg = System.currentTimeMillis();
    Object value = cache.get(key);
    long end = System.currentTimeMillis();

    HttpStatus status = value != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;

    log.debug("cache.get(key): key={}, duration={}, status={}", key, end-beg, status.value());
    return new ResponseEntity(value, status);
  }

  @PostMapping(value = "${controller.hotrod.url}/{key}")
  public ResponseEntity set(@PathVariable String key, @RequestBody String value) {

    long beg = System.currentTimeMillis();
    cache.put(key,value);
    long end = System.currentTimeMillis();

    HttpStatus status = HttpStatus.CREATED;

    log.debug("cache.put(key,value): key={}, duration={}, status={}", key, end-beg, status.value());
    return new ResponseEntity(status);
  }

  @DeleteMapping(value = "${controller.hotrod.url}/{key}")
  public ResponseEntity delete(@PathVariable String key) {
    long beg = System.currentTimeMillis();
    cache.delete(key);
    long end = System.currentTimeMillis();

    HttpStatus status = HttpStatus.OK;

    log.debug("cache.delete(key): key={}, duration={}, status={}", key, end-beg, status.value());
    return new ResponseEntity(status);
  }
}
