package com.binno.dominio.module.usuarioacesso.api;

import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioAcessoDto;
import com.binno.dominio.module.usuarioacesso.service.UsuarioAcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "usuarioacesso")
public class UsuarioAcessoController {

    @Autowired
    private UsuarioAcessoService service;

    @PostMapping(path = "criar")
    public ResponseEntity criarUsuarioAcesso(@RequestBody UsuarioAcessoDto dto) {
        service.criar(dto);
        return ResponseEntity.status(204).build();
    }
}
