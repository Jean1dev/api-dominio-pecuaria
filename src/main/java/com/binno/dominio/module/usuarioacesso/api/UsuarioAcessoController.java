package com.binno.dominio.module.usuarioacesso.api;

import com.binno.dominio.module.usuarioacesso.api.dto.AlterarUsuarioDto;
import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioAcessoDto;
import com.binno.dominio.module.usuarioacesso.service.AlterarDadosUsuarioService;
import com.binno.dominio.module.usuarioacesso.service.UsuarioAcessoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = UsuarioAcessoController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsuarioAcessoController {

    public static final String PATH = "usuarioacesso";

    private final UsuarioAcessoService service;

    private final AlterarDadosUsuarioService alterarDadosUsuarioService;

    @PostMapping(path = "criar")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void criarUsuarioAcesso(@RequestBody @Valid UsuarioAcessoDto dto) {
        service.criar(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void alterarUsuario(@RequestBody AlterarUsuarioDto dto) {
        alterarDadosUsuarioService.executar(dto);
    }
}
