package com.binno.dominio.module.vacinacao.api;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.vacinacao.api.dto.ProcessarVacinacaoDto;
import com.binno.dominio.module.vacinacao.api.dto.ProcessoVacinacaoDto;
import com.binno.dominio.module.vacinacao.repository.ProcessoVacinacaoRepository;
import com.binno.dominio.module.vacinacao.service.ProcessarVacinacao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.binno.dominio.module.vacinacao.api.dto.ProcessoVacinacaoDto.pageToDto;

@RestController
@RequestMapping(ProcessoVacinacaoController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProcessoVacinacaoController {

    public static final String PATH = "vacinacao";

    private final ProcessarVacinacao processarVacinacao;

    private final ProcessoVacinacaoRepository repository;

    private final AuthenticationHolder holder;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void processar(@RequestBody ProcessarVacinacaoDto dto) {
        processarVacinacao.executar(dto);
    }

    @GetMapping
    public Page<ProcessoVacinacaoDto> findAll(@RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                              @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return pageToDto(repository.findAllByTenantId(PageRequest.of(page, size), holder.getTenantId()));
    }
}
