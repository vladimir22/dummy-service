package org.dummy.service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dummy.service.controllers.dto.EchoserverResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
@AllArgsConstructor
public class HttpController {

  private final ObjectMapper objectMapper;

  @GetMapping(value = "${controller.echoserver.url}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity getEchoserverResponse (@RequestBody Optional<String> body, HttpServletRequest request) throws JsonProcessingException {

    Map<String, String> requestHeaders = new HashMap<>();
    Enumeration headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String key = (String) headerNames.nextElement();
      String value = request.getHeader(key);
      requestHeaders.put(key, value);
    }

    EchoserverResponse echoserverResponse = EchoserverResponse.builder()
            .requestURI(request.getRequestURI())
            .requestHeaders(requestHeaders)
            .body(body.orElse(""))
            .build();

    log.info("HTTP: echoserverResponse = '{}'", objectMapper.writeValueAsString(echoserverResponse));

    return new ResponseEntity(echoserverResponse, HttpStatus.OK);
  }

  @PostMapping(value = "${controller.httpclient.url}")
  public String run(@RequestParam String url, @RequestParam Integer threads) throws InterruptedException {
    log.info("start sending requests to url:{}, threads:{}", url, threads);

    final long start = System.currentTimeMillis();

    final int[] responses = {0,0,0};

    ExecutorService service = Executors.newFixedThreadPool(threads);
    CountDownLatch latch = new CountDownLatch(threads);


    RestTemplate restTemplate = new RestTemplate(getClientHttpRequestFactory());
    for (int i = 0; i < threads; i++) {
      service.submit(() -> {
        try{
          ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.GET, null, String.class);
          if (responseEntity.getStatusCode().is2xxSuccessful()){
            responses[0]++;
          } else {
            responses[1]++;
          }
        } catch (Throwable throwable){
          responses[2]++;
          log.error(throwable.getMessage());
        }

        latch.countDown();
      });
    }

   latch.await();

   String response = String.format("url: %1$s, threads: %2$s, success:%3$s, unsuccessful: %4$s, errors: %5$s, duration: %6$s", url, threads, responses[0], responses[1], responses[2], (System.currentTimeMillis() - start));

   log.info("exiting, {}", response);
   return response;
  }


  private ClientHttpRequestFactory getClientHttpRequestFactory() {
    int timeout = 5000;
    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
            = new HttpComponentsClientHttpRequestFactory();
    clientHttpRequestFactory.setConnectTimeout(timeout);
    return clientHttpRequestFactory;
  }

}
