package com.binno.dominio.module.usuarioacesso.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioAcessoDto;
import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioTenantDto;
import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import com.binno.dominio.provider.mail.MailProvider;
import com.binno.dominio.provider.mail.SendEmailPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsuarioAcessoService {

    private final UsuarioAcessoRepository repository;

    private final AuthenticationHolder holder;

    private final TenantRepository tenantRepository;

    private final MailProvider mailProvider;

    public void criarUsuarioCasoNaoExista(UsuarioAcessoDto dto) {
        Optional<UsuarioAcesso> usuarioAcesso = repository.findByLogin(dto.getLogin());

        if (usuarioAcesso.isEmpty()) {
            criar(dto);
        }
    }

    public void criarUsuarioAcessoParaTenantExistente(UsuarioAcessoDto dto) {
        criar(UsuarioAcessoDto.builder()
                .tenant(holder.getTenantId())
                .fone(dto.getFone())
                .sobrenome(dto.getSobrenome())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .photoURL(dto.getPhotoURL())
                .password(dto.getPassword())
                .build());
    }

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
                .photoURL(dto.getPhotoURL())
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
                .imagemPerfilUrl(dto.getPhotoURL())
                .ativo(true)
                .build();
    }

    public Page<UsuarioTenantDto> meusUsuarios(PageRequest pageRequest) {
        return UsuarioTenantDto.pageToDto(repository.findAllByTenantId(pageRequest, holder.getTenantId()));
    }
}
