package org.dummy.service.controllers.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DbStatus {
    String jdbcUrl;
    String username;
    String password;
    boolean isValid;
    String errorMessage;
}
