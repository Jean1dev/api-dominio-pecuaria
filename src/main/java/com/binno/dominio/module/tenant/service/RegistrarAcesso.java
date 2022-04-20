package com.binno.dominio.module.tenant.service;

import com.binno.dominio.auth.UsuarioAutenticado;
import com.binno.dominio.context.AuthenticationHolder;
import com.binno.dominio.module.tenant.model.Acessos;
import com.binno.dominio.module.tenant.model.Tenant;
import com.binno.dominio.module.tenant.repository.AcessoRepository;
import com.binno.dominio.shared.RegraNegocioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrarAcesso implements RegraNegocioService<Acessos, UsuarioAutenticado> {

    private final AcessoRepository repository;

    private final AuthenticationHolder holder;

    @Override
    public Acessos executar(UsuarioAutenticado usuarioAutenticado) {
        return repository.save(Acessos.builder()
                .dataHoraAcesso(LocalDateTime.now())
                .login(usuarioAutenticado.getLogin())
                .tenant(Tenant.of(usuarioAutenticado.getTenantId()))
                .dispositivo(holder.getDispositivo())
                .ip(holder.getIp())
                .build());
    }
}
