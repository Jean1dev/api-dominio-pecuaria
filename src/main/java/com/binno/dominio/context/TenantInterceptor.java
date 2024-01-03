package com.binno.dominio.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class TenantInterceptor implements HandlerInterceptor {

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
