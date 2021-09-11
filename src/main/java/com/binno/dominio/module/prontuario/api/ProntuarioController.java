package com.binno.dominio.module.prontuario.api;

import com.binno.dominio.module.prontuario.api.dto.DadosProntuarioDto;
import com.binno.dominio.module.prontuario.service.GerarDadosProntuario;
import com.binno.dominio.module.prontuario.service.GerarProntuarioPDF;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping(ProntuarioController.PATH)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProntuarioController {

    public static final String PATH = "prontuario";

    private final GerarDadosProntuario gerarDadosProntuario;

    @GetMapping(path = "imprimir", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> gerarPdfProntuario(@RequestParam(value = "animalId") Integer animalId) throws DocumentException {
        DadosProntuarioDto prontuarioDto = gerarDadosProntuario.executar(animalId);
        ByteArrayInputStream inputStream = GerarProntuarioPDF.gerar(prontuarioDto);

        var headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=prontuario.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(inputStream));
    }
}
