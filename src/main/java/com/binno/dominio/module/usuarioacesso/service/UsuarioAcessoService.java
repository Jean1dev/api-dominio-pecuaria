package com.binno.dominio.module.usuarioacesso.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioAcessoDto;
import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Transactional
public class UsuarioAcessoService {

    @Autowired
    private UsuarioAcessoRepository repository;

    @Autowired
    private AuthenticationHolder holder;

    @Autowired
    private TenantRepository tenantRepository;

    public void criar(UsuarioAcessoDto dto) {
        Tenant tenant = tenantRepository.findById(Objects.isNull(dto.getTenant()) ? 99999 : dto.getTenant()).orElseGet(() -> tenantRepository.save(Tenant.builder()
                .nome(dto.getLogin())
                .ativo(true)
                .build()));

        repository.save(criarUsuarioAcesso(UsuarioAcessoDto.builder()
                .login(dto.getLogin())
                .password(dto.getPassword())
                .tenant(tenant.getId())
                .build()));
    }

    public UsuarioAcesso criarUsuarioAcesso(UsuarioAcessoDto dto) {
        return UsuarioAcesso.builder()
                .login(dto.getLogin())
                .password(new BCryptPasswordEncoder().encode(dto.getPassword()))
                .tenant(Tenant.builder().id(dto.getTenant()).build())
                .ativo(true)
                .build();
    }
}
