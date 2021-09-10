package com.binno.dominio.module.notificacao.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.notificacao.api.dto.NotificacaoDto;
import com.binno.dominio.module.notificacao.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.binno.dominio.module.notificacao.api.dto.NotificacaoDto.listToDto;

@RestController
@RequestMapping(NotificacaoController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class NotificacaoController {

    public static final String PATH = "notificacao";

    private final NotificacaoRepository repository;

    private final AuthenticationHolder holder;

    @GetMapping
    public List<NotificacaoDto> list() {
        return listToDto(repository.findAllByTenantIdOrderByIdDesc(holder.getTenantId()));
    }

    @GetMapping(path = "tenho-notificacoes")
    public long verificarMinhasNotificacoes() {
        return repository.countAllByTenantId(holder.getTenantId());
    }

    @PutMapping(path = "marcar-como-lida/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void marcarComoLida(@PathVariable("id") Integer id) {
        repository.deleteById(id);
    }

    @PostMapping(path = "marcar-todos-como-lidas")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void marcarTodasComoLidas() {
        repository.deleteAll(repository.findAllByTenantId(holder.getTenantId()));
    }
}
