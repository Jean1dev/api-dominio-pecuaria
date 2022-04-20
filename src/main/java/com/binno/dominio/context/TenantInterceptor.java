package com.binno.dominio.context;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TenantInterceptor extends HandlerInterceptorAdapter {

    private final AuthenticationHolder holder;

    public TenantInterceptor(AuthenticationHolder holder) {
        this.holder = holder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        holder.setTenantId(getTenant(request));
        holder.setUserAccess(getUserAccess(request));
        holder.setDispositivo(request.getHeader("dispositivo"));
        holder.setIp(request.getRemoteHost());
        return true;
    }

    private Integer getUserAccess(HttpServletRequest request) {
        return (Integer) request.getAttribute("user_access");
    }

    private Integer getTenant(HttpServletRequest request) {
        return (Integer) request.getAttribute("tenant");
    }
}
