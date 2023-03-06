package org.dummy.service.controllers.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class HotrodResponse {
    Map<String, Object> entries;
    String errorMessage;
}
