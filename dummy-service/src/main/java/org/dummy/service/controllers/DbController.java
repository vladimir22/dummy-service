package org.dummy.service.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dummy.service.controllers.dto.DbResponse;
import org.dummy.service.controllers.dto.DbStatus;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@RestController
@Slf4j
@AllArgsConstructor
public class DbController {


    private final DataSource dataSource;
    private final DataSourceProperties dataSourceProperties;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;

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

    static String hidePassword(String password) {
        if (password == null) {
            return "null";
        }
        if (password.isBlank() || password.length() == 1) {
            return password;
        }
        return password.charAt(0) + "***" + password.charAt(password.length()-1);
    }


    @PostMapping(value = "${controller.db.url}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity executeSql(@RequestParam String query) throws JsonProcessingException {

        log.info("Starting execute DB SQL: query='{}'", query);

        ResultSet resultSet = jdbcTemplate.execute(query,new PreparedStatementCallback<ResultSet>(){
            @Override
            public ResultSet doInPreparedStatement(PreparedStatement ps)
                    throws SQLException {
//                ps.setInt(1,e.getId());
//                ps.setString(2,e.getName());
//                ps.setFloat(3,e.getSalary());
                return ps.getResultSet();

            }
        });
        DbResponse dbResponse = new DbResponse(resultSet);
        log.info("Finish executing DB SQL: query='{}', dbResponse='{}'", query, dbResponse);
        return new ResponseEntity(dbResponse, HttpStatus.OK);
    }

}
