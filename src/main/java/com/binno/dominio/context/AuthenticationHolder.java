package com.binno.dominio.context;

import lombok.*;
import org.springframework.stereotype.Service;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationHolder {

    private Integer tenantId;

    private Integer UserAccess;
}
