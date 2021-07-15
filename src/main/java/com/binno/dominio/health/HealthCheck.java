package com.binno.dominio.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "health-check")
public class HealthCheck {

    @GetMapping
    public String healthCheck() {
        return "UP";
    }
}
