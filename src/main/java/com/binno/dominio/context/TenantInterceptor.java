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
        Integer tenant = getTenant(request);
        Integer userAccess = getUserAccess(request);

        holder.setTenantId(tenant);
        holder.setUserAccess(userAccess);

        return true;
    }

    private Integer getUserAccess(HttpServletRequest request) {
        Integer userAccess = (Integer) request.getAttribute("user_access");
        return userAccess;
    }

    private Integer getTenant(HttpServletRequest request) {
        Integer tenant = (Integer) request.getAttribute("tenant");
        return tenant;
    }
}
