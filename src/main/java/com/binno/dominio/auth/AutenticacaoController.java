package com.binno.dominio.auth;

import com.binno.dominio.auth.dto.LoginDto;
import com.binno.dominio.auth.dto.LoginGoogleDto;
import com.binno.dominio.auth.dto.TokenDto;
import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioAcessoDto;
import com.binno.dominio.module.usuarioacesso.service.UsuarioAcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = AutenticacaoController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AutenticacaoController {

    public static final String PATH = "auth";

    private final AuthenticationManager manager;

    private final TokenService tokenService;

    private final UsuarioAcessoService usuarioAcessoService;

    @PostMapping
    public ResponseEntity<TokenDto> autenticar(@RequestBody LoginDto form) {
        UsernamePasswordAuthenticationToken dadosLogin = form.converter();
        return login(dadosLogin);
    }

    @PostMapping(path = "google")
    public ResponseEntity<TokenDto> autenticarViaGoogle(@RequestBody LoginGoogleDto form) {
        UsuarioAcessoDto acessoDto = UsuarioAcessoDto.builder()
                .nome(form.getNome())
                .email(form.getEmail())
                .login(form.getEmail())
                .password(form.getUid())
                .photoURL(form.getPhotoURL())
                .build();

        usuarioAcessoService.criarUsuarioCasoNaoExista(acessoDto);
        return login(new UsernamePasswordAuthenticationToken(acessoDto.getEmail(), acessoDto.getPassword()));
    }

    private ResponseEntity<TokenDto> login(UsernamePasswordAuthenticationToken dadosLogin) {
        try {
            Authentication authentication = manager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);
            return ResponseEntity.ok(new TokenDto(token, "Bearer", (UsuarioAutenticado) authentication.getPrincipal()));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
