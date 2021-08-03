package com.binno.dominio.provider.mail;

public interface MailProvider {

    void send(SendEmailPayload payload);

    String getFrom();
}
