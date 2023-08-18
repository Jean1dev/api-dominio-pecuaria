package com.binno.dominio.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AutenticacaoService  implements UserDetailsService {

    private final UsuarioAutenticadoService service;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return service.find(s);
    }
}
