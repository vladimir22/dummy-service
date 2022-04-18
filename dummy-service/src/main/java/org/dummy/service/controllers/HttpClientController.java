package org.dummy.service.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@Slf4j
public class HttpClientController {

  @GetMapping(value = "/status")
  public String getStatus() {
    log.info("status check: service is available");
    return "Service is available";
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
