package com.binno.dominio.config;

import com.binno.dominio.auth.AutenticacaoService;
import com.binno.dominio.auth.AutenticacaoViaTokenFilter;
import com.binno.dominio.auth.TokenService;
import com.binno.dominio.auth.UsuarioAutenticadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    @Autowired
    private AutenticacaoService autenticacaoService;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManager = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManager.userDetailsService(autenticacaoService).passwordEncoder(new BCryptPasswordEncoder());

        return authenticationManager.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                            //.requestMatchers("/swagger-ui/index.html").permitAll()
                            .requestMatchers("/swagger-ui*").permitAll()
                            //.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                            .requestMatchers(HttpMethod.POST, "/usuarioacesso/criar").permitAll()
                            .requestMatchers(HttpMethod.POST, "/usuarioacesso/alterar-senha").permitAll()
                            .requestMatchers(HttpMethod.GET, "/health-check").permitAll()
                            .requestMatchers(HttpMethod.GET, "/prontuario/imprimir").permitAll()
                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .headers(headers -> {
                    headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
                })
                .addFilterBefore(new AutenticacaoViaTokenFilter(tokenService, usuarioAutenticadoService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
