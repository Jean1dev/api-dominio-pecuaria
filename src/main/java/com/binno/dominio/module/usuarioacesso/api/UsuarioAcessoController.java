package com.binno.dominio.module.usuarioacesso.api;

import com.binno.dominio.module.usuarioacesso.api.dto.AlterarSenhaDto;
import com.binno.dominio.module.usuarioacesso.api.dto.AlterarUsuarioDto;
import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioAcessoDto;
import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioTenantDto;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import com.binno.dominio.module.usuarioacesso.service.AlterarDadosUsuarioService;
import com.binno.dominio.module.usuarioacesso.service.UsuarioAcessoService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.binno.dominio.module.usuarioacesso.api.dto.UsuarioAcessoDto.toDto;

@RestController
@RequestMapping(path = UsuarioAcessoController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsuarioAcessoController {

    public static final String PATH = "usuarioacesso";

    private final UsuarioAcessoService service;

    private final AlterarDadosUsuarioService alterarDadosUsuarioService;

    private final UsuarioAcessoRepository repository;

    @GetMapping(path = "meus-usuarios")
    public Page<UsuarioTenantDto> meusUsuarios(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return service.meusUsuarios(PageRequest.of(page, size));
    }

    @PostMapping(path = "criar-para-tenant-existente")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void criarUsuarioAcessoParaTenantExistente(@RequestBody @Valid UsuarioAcessoDto dto) {
        service.criarUsuarioAcessoParaTenantExistente(dto);
    }

    @PostMapping(path = "alterar-senha")
    public ResponseEntity<String> alterarSenha(@RequestBody @Valid AlterarSenhaDto dto) {
        if (!service.alterarSenha(dto.getChave(), dto.getNovaSenha())) {
            return ResponseEntity.badRequest().body("Tempo de alteração de senha expirado");
        }

        return ResponseEntity.ok("Alterado com sucesso");
    }

    @PostMapping(path = "solicitar-alteracao-senha")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void solicitarAlteracaoSenha(@RequestBody JsonNode body) {
        String login = body.get("login").asText();
        service.solicitarAlteracaoSenha(login);
    }

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

    @GetMapping
    public UsuarioAcessoDto getInformacoesUsuario(@RequestParam(value = "usuarioId") Integer id) {
        return toDto(repository.findById(id).orElseThrow());
    }
}
