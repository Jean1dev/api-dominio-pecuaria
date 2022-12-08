package com.binno.dominio.module.usuarioacesso.service;

import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.notificacao.service.RegistrarNotificacao;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.TenantRepository;
import com.binno.dominio.module.usuarioacesso.api.dto.AlterarUsuarioDto;
import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioAcessoDto;
import com.binno.dominio.module.usuarioacesso.api.dto.UsuarioTenantDto;
import com.binno.dominio.module.usuarioacesso.model.AlteracaoSenha;
import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.AlteracaoSenhaRepository;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UsuarioAcessoService {

    private final UsuarioAcessoRepository repository;
    private final AuthenticationHolder holder;
    private final TenantRepository tenantRepository;
    private final MailProvider mailProvider;
    private final AlteracaoSenhaRepository alteracaoSenhaRepository;
    private final RegistrarNotificacao registrarNotificacao;
    private final AlterarDadosUsuarioService alterarDadosUsuarioService;

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

        enviarEmailSolicitandoQueUsuarioAltereSuaSenha(dto.getEmail(), dto.getLogin());
    }

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void solicitarAlteracaoSenha(String login) {
        UsuarioAcesso usuarioAcesso = repository.findByLogin(login).orElseThrow();
        alteracaoSenhaRepository.findByLogin(login).ifPresent(alteracaoSenhaRepository::delete);
        enviarEmailSolicitandoQueUsuarioAltereSuaSenha(usuarioAcesso.getEmail(), usuarioAcesso.getLogin());
    }

    private void enviarEmailSolicitandoQueUsuarioAltereSuaSenha(String email, String login) {
        UUID chave = UUID.randomUUID();
        AlteracaoSenha alteracaoSenha = alteracaoSenhaRepository.save(AlteracaoSenha.builder()
                .chave(chave.toString())
                .dataHoraCriacao(LocalDateTime.now())
                .timeExpiracao(Duration.ofHours(8))
                .email(email)
                .login(login)
                .build());

        final String link = "https://binno-agro.netlify.app/alterar-senha/" + alteracaoSenha.getChave();
        mailProvider.send(SendEmailPayload.builder()
                .from(mailProvider.getFrom())
                .to(email)
                .subject("Alteração de senha pendente")
                .text("Olá utilize o link: " + link + " e altere a sua senha, o link expira em 8 horas")
                .build());

        registrarNotificacao.executar("Enviado email para alteração de senha para o usuario " + login);
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
                .contaPublica(true)
                .build();
    }

    public Page<UsuarioTenantDto> meusUsuarios(PageRequest pageRequest) {
        return UsuarioTenantDto.pageToDto(repository.findAllByTenantId(pageRequest, holder.getTenantId()));
    }

    public boolean alterarSenha(String chave, String novaSenha) {
        AlteracaoSenha alteracaoSenha = alteracaoSenhaRepository.findByChave(chave).orElseThrow();
        LocalDateTime prazoFinalAlteracao = alteracaoSenha.getDataHoraCriacao().plus(alteracaoSenha.getTimeExpiracao());

        if (LocalDateTime.now().isAfter(prazoFinalAlteracao)) {
            alteracaoSenhaRepository.delete(alteracaoSenha);
            return false;
        }

        UsuarioAcesso usuarioAcesso = repository.findByLogin(alteracaoSenha.getLogin()).orElseThrow();
        alterarDadosUsuarioService.executar(AlterarUsuarioDto.builder()
                .id(usuarioAcesso.getId())
                .login(usuarioAcesso.getLogin())
                .numero(usuarioAcesso.getNumero())
                .nome(usuarioAcesso.getNome())
                .imagemPerfilUrl(usuarioAcesso.getImagemPerfilUrl())
                .email(alteracaoSenha.getEmail())
                .password(novaSenha)
                .sobrenome(usuarioAcesso.getSobrenome())
                .build());

        alteracaoSenhaRepository.delete(alteracaoSenha);
        return true;
    }

}
