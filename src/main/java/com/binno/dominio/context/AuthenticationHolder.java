package com.binno.dominio.context;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationHolder {

    private Integer tenantId;
    private Integer UserAccess;
    private String ip;
    private String dispositivo;
}
