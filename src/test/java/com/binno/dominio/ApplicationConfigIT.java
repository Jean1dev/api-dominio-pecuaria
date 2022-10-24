package com.binno.dominio;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active:test")
@AutoConfigureMockMvc
public abstract class ApplicationConfigIT {
}
