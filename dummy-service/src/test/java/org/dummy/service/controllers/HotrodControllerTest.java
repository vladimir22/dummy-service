package org.dummy.service.controllers;

import org.dummy.service.cache.HotRodCache;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class HotrodControllerTest {

  @Value("${controller.hotrod.url}")
  public String APP_URL;

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

  @Autowired
  private MockMvc mockMvc;

  @Test
  void get() throws Exception {

    String key = "test_key";
    String value = "test_value";

    // get empty value
    MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(APP_URL.replace("{key}", key))
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .accept(MediaType.TEXT_PLAIN_VALUE)
    )
            .andExpect(status().isNotFound())
            .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    assertThat(responseBody, emptyOrNullString());

    // set value
    mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(APP_URL.replace("{key}", key))
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .accept(MediaType.TEXT_PLAIN_VALUE)
            .content(value)
    )
            .andExpect(status().isCreated())
            .andReturn();
    responseBody = mvcResult.getResponse().getContentAsString();
    assertThat(responseBody, emptyOrNullString());

    // get value
    mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(APP_URL.replace("{key}", key))
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .accept(MediaType.TEXT_PLAIN_VALUE)
    )
            .andExpect(status().isOk())
            .andReturn();
    responseBody = mvcResult.getResponse().getContentAsString();
    assertThat(responseBody, equalTo(value));

    // delete value
    mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(APP_URL.replace("{key}", key))
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .accept(MediaType.TEXT_PLAIN_VALUE)
            .content(value)
    )
            .andExpect(status().isOk())
            .andReturn();
    responseBody = mvcResult.getResponse().getContentAsString();
    assertThat(responseBody, emptyOrNullString());

    // delete empty value
    mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(APP_URL.replace("{key}", key))
            .contentType(MediaType.TEXT_PLAIN_VALUE)
            .accept(MediaType.TEXT_PLAIN_VALUE)
    )
            .andExpect(status().isNotFound())
            .andReturn();
    responseBody = mvcResult.getResponse().getContentAsString();
    assertThat(responseBody, emptyOrNullString());
  }
}
