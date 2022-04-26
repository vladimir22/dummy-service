package org.dummy.service.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dummy.service.controllers.dto.EchoserverResponse;
import org.junit.jupiter.api.Test;
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
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class HttpControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Value("${controller.echoserver.url}")
    private String url;

    @Test
    void getEchoserverResponse() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("test_name", "test_value")

                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        EchoserverResponse echoserverResponse = objectMapper.readValue(responseBody, EchoserverResponse.class);

        assertThat(echoserverResponse.getRequestURI(), equalTo(url));
        assertThat(echoserverResponse.getRequestHeaders().get("test_name"), equalTo("test_value"));
    }

}