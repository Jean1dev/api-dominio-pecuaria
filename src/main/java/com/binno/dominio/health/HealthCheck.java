package com.binno.dominio.health;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "health-check")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HealthCheck {

    private final HealthCheckRepository healthCheckRepository;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String healthCheck() {

        Integer integer = healthCheckRepository.connectionBD();

        return "{ 'Aplication': 'UP','DB:" + (integer.equals(1) ? "'UP'" : "'DOWN'") + "}";
    }
}
