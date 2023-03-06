package org.dummy.service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dummy.service.controllers.dto.DbStatus;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.SQLException;


@RestController
@Slf4j
@AllArgsConstructor
public class DbController {


    private final DataSource dataSource;
    private final DataSourceProperties dataSourceProperties;
    private final ObjectMapper objectMapper;

    @GetMapping(value = "${controller.db.url}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getDbStatus() throws JsonProcessingException {

        DbStatus dbStatus = DbStatus.builder()
                .jdbcUrl(dataSourceProperties.getUrl())
                .username(dataSourceProperties.getUsername())
                .password(hidePassword(dataSourceProperties.getPassword()))
                .build();
        try {
            dbStatus.setValid(dataSource.getConnection().isValid(1000));
        } catch (SQLException e) {
            log.error("DB status check: error = '{}'",e.getMessage(), e);
            dbStatus.setValid(false);
            dbStatus.setErrorMessage(e.getMessage());
        }

        log.info("DB status check: dbStatus = '{}'", objectMapper.writeValueAsString(dbStatus));
        return new ResponseEntity(dbStatus, HttpStatus.OK);
    }

    public static String hidePassword(String password) {
        if (password == null) {
            return "null";
        }
        if (password.isBlank() || password.length() == 1) {
            return password;
        }

        return password.charAt(0) + "***" + password.charAt(password.length()-1);
    }
}
