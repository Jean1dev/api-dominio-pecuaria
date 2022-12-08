package com.binno.dominio.module.usuarioacesso.api;

import com.binno.dominio.module.usuarioacesso.api.dto.*;
import com.binno.dominio.module.usuarioacesso.model.ConviteAmizade;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import com.binno.dominio.module.usuarioacesso.service.AlterarDadosUsuarioService;
import com.binno.dominio.module.usuarioacesso.service.AmizadesService;
import com.binno.dominio.module.usuarioacesso.service.UsuarioAcessoService;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.binno.dominio.module.usuarioacesso.api.dto.UsuarioAcessoDto.toDto;

@RestController
@RequestMapping(path = UsuarioAcessoController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsuarioAcessoController {

    public static final String PATH = "usuarioacesso";

    private final UsuarioAcessoService service;

    private final AlterarDadosUsuarioService alterarDadosUsuarioService;

    private final UsuarioAcessoRepository repository;

    private final AmizadesService amizadesService;

    @PostMapping(path = "solicitar-amizade")
    @ApiOperation("Solicita um convite de amizade")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void solicitarAmizade(@RequestBody @Valid CriarPedidoAmizadeDto dto) {
        amizadesService.criarPedidoAmizade(dto);
    }

    @GetMapping(path = "meus-convites-amizades")
    @ApiOperation("Retorna a lista de convites")
    public List<ConviteAmizade> meusConvites(@RequestParam(value = "idUsuario") int idUsuario) {
        return amizadesService.meusConvites(idUsuario);
    }

    @GetMapping(path = "meus-amigos")
    @ApiOperation("Retorna a lista dos seus amigos")
    public List<UsuarioAcessoDto> meusAmigos() {
        return amizadesService.meusAmigos().stream().map(UsuarioAcessoDto::toDto).collect(Collectors.toList());
    }

    @PutMapping(path = "aceitar-pedido/{id}")
    @ApiOperation("Aceita um pedido de amizade")
    public void aceitarPedido(@PathVariable("id") int idPedido) {
        amizadesService.aceitarPedidoAmizade(idPedido);
    }

    @GetMapping(path = "meus-usuarios")
    @ApiOperation("Retorna a lista de usuarios do tenant")
    public Page<UsuarioTenantDto> meusUsuarios(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return service.meusUsuarios(PageRequest.of(page, size));
    }

    @PostMapping(path = "criar-para-tenant-existente")
    @ApiOperation("Cria um novo usuario para o tenant existente")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void criarUsuarioAcessoParaTenantExistente(@RequestBody @Valid UsuarioAcessoDto dto) {
        service.criarUsuarioAcessoParaTenantExistente(dto);
    }

    @PostMapping(path = "alterar-senha")
    @ApiOperation("Alterar senha")
    public ResponseEntity<String> alterarSenha(@RequestBody @Valid AlterarSenhaDto dto) {
        if (!service.alterarSenha(dto.getChave(), dto.getNovaSenha())) {
            return ResponseEntity.badRequest().body("Tempo de alteração de senha expirado");
        }

        return ResponseEntity.ok("Alterado com sucesso");
    }

    @PostMapping(path = "solicitar-alteracao-senha")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation("Solicitar alteracao de senha")
    public void solicitarAlteracaoSenha(@RequestBody JsonNode body) {
        String login = body.get("login").asText();
        service.solicitarAlteracaoSenha(login);
    }

    @PostMapping(path = "criar")
    @ApiOperation("Cria um novo usuario")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void criarUsuarioAcesso(@RequestBody @Valid UsuarioAcessoDto dto) {
        service.criar(dto);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ApiOperation("Alterar usuario")
    public void alterarUsuario(@RequestBody AlterarUsuarioDto dto) {
        alterarDadosUsuarioService.executar(dto);
    }

    @GetMapping
    @ApiOperation("Retorna informações do usuario logado")
    public UsuarioAcessoDto getInformacoesUsuario(@RequestParam(value = "usuarioId") Integer id) {
        return toDto(repository.findById(id).orElseThrow());
    }
}
