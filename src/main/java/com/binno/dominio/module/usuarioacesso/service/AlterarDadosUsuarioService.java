package com.binno.dominio.module.usuarioacesso.service;

import com.binno.dominio.module.usuarioacesso.api.dto.AlterarUsuarioDto;
import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import com.binno.dominio.module.usuarioacesso.repository.UsuarioAcessoRepository;
import com.binno.dominio.provider.mail.MailProvider;
import com.binno.dominio.provider.mail.SendEmailPayload;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlterarDadosUsuarioService implements RegraNegocioService<UsuarioAcesso, AlterarUsuarioDto> {

    private final UsuarioAcessoRepository repository;

    private final MailProvider mailProvider;

    @Override
    public UsuarioAcesso executar(AlterarUsuarioDto dto) {
        UsuarioAcesso usuarioAcesso = repository.findById(dto.getId()).orElseThrow();

        if (dto.getAlterarSomenteAImagem()) {
            return alterarSomenteAImagem(usuarioAcesso, dto.getImagemPerfilUrl());
        }

        usuarioAcesso.setNome(dto.getNome());
        usuarioAcesso.setSobrenome(dto.getSobrenome());
        usuarioAcesso.setNumero(dto.getNumero());

        verificarSePodeAlterarLoginSePuderAlterar(usuarioAcesso, dto.getLogin());
        seAlterouEmailNotificar(usuarioAcesso, dto.getEmail());
        seAlterouSenhaCriptografar(usuarioAcesso, dto.getPassword());
        return repository.save(usuarioAcesso);
    }

    private UsuarioAcesso alterarSomenteAImagem(UsuarioAcesso usuarioAcesso, String imagemPerfilUrl) {
        usuarioAcesso.setImagemPerfilUrl(imagemPerfilUrl);
        return repository.save(usuarioAcesso);
    }

    private void verificarSePodeAlterarLoginSePuderAlterar(UsuarioAcesso usuarioAcesso, String login) {
        repository.findByLogin(login).ifPresent(usuarioEncontrado -> {
            if (!usuarioEncontrado.getId().equals(usuarioAcesso.getId()))
                throw new ValidationException("Já existe um usuario utilizando esse Login");
        });

        usuarioAcesso.setLogin(login);
    }

    private void seAlterouSenhaCriptografar(UsuarioAcesso usuarioAcesso, String password) {
        if (Objects.isNull(password))
            return;

        usuarioAcesso.setPassword(new BCryptPasswordEncoder().encode(password));
    }

    private void seAlterouEmailNotificar(UsuarioAcesso usuarioAcesso, String email) {
        if (email.equals(usuarioAcesso.getEmail()))
            return;

        usuarioAcesso.setEmail(email);
        mailProvider.send(SendEmailPayload.builder()
                .from(mailProvider.getFrom())
                .to(email)
                .subject("Informações referente sua conta Binno")
                .text("Email alterado com sucesso")
                .build());
    }
}
