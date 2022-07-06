package com.binno.dominio.module.comercializacao.http.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CambioDto implements Serializable {
    private String id;
    private Double valor;
    private LocalDateTime lastTimeUpdated;
    private String code;
    private String codein;
    private String name;
    private Double high;
    private Double low;
    private Double varBid;
    private Double pctChange;
    private Double bid;
    private Double ask;
    private long timestamp;
}
