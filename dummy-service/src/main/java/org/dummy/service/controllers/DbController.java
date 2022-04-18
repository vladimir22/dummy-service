package org.dummy.service.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
public class DbController {

    @GetMapping(value = "${controller.db.url}")
    public String getStatus() {
        log.info("status check: service is available");
        return "DB is available";
    }
}
