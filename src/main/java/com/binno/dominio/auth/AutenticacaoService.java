package com.binno.dominio.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacaoService  implements UserDetailsService {

    @Autowired
    private UsuarioAutenticadoService service;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return service.find(s);
    }
}
