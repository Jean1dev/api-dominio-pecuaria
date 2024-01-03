package com.binno.dominio.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    private final UsuarioAutenticadoService service;

    public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioAutenticadoService service) {
        this.tokenService = tokenService;
        this.service = service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException, ServletException {
        String token = recuperarToken(httpServletRequest);
        boolean valido = tokenService.isTokenValido(token);
        if (valido) {
            autenticarCliente(token, httpServletRequest);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private void autenticarCliente(String token, HttpServletRequest httpServletRequest) {
        String login = tokenService.getLogin(token);
        UsuarioAutenticado usuario = service.find(login);
        httpServletRequest.setAttribute("tenant", usuario.getTenantId());
        httpServletRequest.setAttribute("user_access", usuario.getId());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recuperarToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        return token.substring(7, token.length());
    }
}
