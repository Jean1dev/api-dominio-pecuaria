package com.binno.dominio;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
public abstract class ApplicationConfigIT {
}
