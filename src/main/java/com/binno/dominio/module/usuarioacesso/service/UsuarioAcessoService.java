package com.binno.dominio.module.usuarioacesso.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioAcessoDto;
import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import com.binno.dominio.provider.mail.MailProvider;
import com.binno.dominio.provider.mail.SendEmailPayload;
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

    @Autowired
    private MailProvider mailProvider;

    public void criar(UsuarioAcessoDto dto) {
        Tenant tenant = tenantRepository.findById(Objects.isNull(dto.getTenant()) ? 99999 : dto.getTenant()).orElseGet(() -> tenantRepository.save(Tenant.builder()
                .nome(dto.getLogin())
                .ativo(true)
                .build()));

        repository.save(criarUsuarioAcesso(UsuarioAcessoDto.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .password(dto.getPassword())
                .tenant(tenant.getId())
                .build()));

        mailProvider.send(SendEmailPayload.builder()
                .from(mailProvider.getFrom())
                .to(dto.getEmail())
                .subject("Conta criada com sucesso")
                .text("Olá " + dto.getNome() + " sua conta foi criado com sucesso")
                .build());
    }

    public UsuarioAcesso criarUsuarioAcesso(UsuarioAcessoDto dto) {
        return UsuarioAcesso.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .password(new BCryptPasswordEncoder().encode(dto.getPassword()))
                .tenant(Tenant.of(dto.getTenant()))
                .ativo(true)
                .build();
    }
}
