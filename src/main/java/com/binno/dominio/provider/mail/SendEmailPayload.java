package com.binno.dominio.provider.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor
public class SendEmailPayload implements Serializable {

    private String from;
    private String to;
    private String subject;
    private String text;
}
