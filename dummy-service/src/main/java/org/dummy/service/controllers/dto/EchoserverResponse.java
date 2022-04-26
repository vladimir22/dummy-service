package org.dummy.service.controllers.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class EchoserverResponse {
    Map<String, String> requestHeaders;
    String body;
    String requestURI;
}
