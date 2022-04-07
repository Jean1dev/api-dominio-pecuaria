package com.binno.dominio.module.usuarioacesso.api.dto;

import com.binno.dominio.module.usuarioacesso.model.UsuarioAcesso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class UsuarioTenantDto {

    private final String login;
    private final String email;
    private final String imagemUrl;

    public static Page<UsuarioTenantDto> pageToDto(Page<UsuarioAcesso> page) {
        List<UsuarioTenantDto> dtos = listToDto(page.getContent());
        return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
    }

    public static List<UsuarioTenantDto> listToDto(List<UsuarioAcesso> usuarioAcessos) {
        return usuarioAcessos.stream().map(UsuarioTenantDto::toDto).collect(Collectors.toList());
    }

    public static UsuarioTenantDto toDto(UsuarioAcesso usuarioAcesso) {
        return UsuarioTenantDto.builder()
                .email(usuarioAcesso.getEmail())
                .login(usuarioAcesso.getLogin())
                .imagemUrl(usuarioAcesso.getImagemPerfilUrl())
                .build();
    }
}
