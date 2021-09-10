package com.binno.dominio.module.prontuario.service;

import com.binno.dominio.module.animal.model.Animal;
import com.binno.dominio.module.animal.model.PesoAnimal;
import com.binno.dominio.module.prontuario.api.dto.DadosProntuarioDto;
import com.binno.dominio.module.vacinacao.model.ProcessoVacinacao;
import com.itextpdf.text.*;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class GerarProntuarioPDF {

    private static final Logger logger = LoggerFactory.getLogger(GerarProntuarioPDF.class);

    public static ByteArrayInputStream gerar(DadosProntuarioDto dto) throws DocumentException {
        Document document = new Document();
        logger.info("gerando PDF");

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();

        addCabecalho(document);
        addInformacoesAnimal(document, dto.getAnimal());
        addDivisorProcessosVacinacoes(document);
        addProcessosVacinacoes(document, dto.getProcessoVacinacaoList());

        document.close();
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    private static void addProcessosVacinacoes(Document document, List<ProcessoVacinacao> processoVacinacaoList) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{2, 2});
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell hcell = new PdfPCell(new Phrase("Data aplicação", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        hcell = new PdfPCell(new Phrase("Medicamento", headFont));
        hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(hcell);

        processoVacinacaoList.forEach(processoVacinacao -> {
            PdfPCell cell;
            cell = new PdfPCell(new Phrase(processoVacinacao.getDataProcesso().format(DateTimeFormatter.ofPattern("dd/MM/uuuu"))));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(processoVacinacao.getMedicamento().getNome()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);
        });

        document.add(table);
    }

    private static void addDivisorProcessosVacinacoes(Document document) throws DocumentException {
        PdfPTable tableHeader = new PdfPTable(1);
        tableHeader.setWidthPercentage(100);
        tableHeader.setWidths(new int[]{4});
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        Phrase header = new Phrase("Vacinações aplicadas", headFont);
        PdfPCell pCell = new PdfPCell(header);
        pCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pCell.setBackgroundColor(getCinza());
        tableHeader.addCell(pCell);
        document.add(tableHeader);
    }

    private static void addInformacoesAnimal(Document document, Animal animal) throws DocumentException {
        PdfPTable tableHeader = new PdfPTable(2);
        tableHeader.setWidthPercentage(100);
        tableHeader.setWidths(new int[]{2, 4});
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        PdfPCell pCell = new PdfPCell(new Phrase("Animal numero", headFont));
        pCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tableHeader.addCell(pCell);
        tableHeader.addCell(new PdfPCell(new Phrase(animal.getNumero() + " : " + animal.getApelido())));

        if (!animal.getPesoAnimal().isEmpty()) {
            PdfPCell pCell1 = new PdfPCell(new Phrase("Ultima pesagem", headFont));
            pCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableHeader.addCell(pCell1);

            PesoAnimal ultimaPesagem = animal.getPesoAnimal().get(animal.getPesoAnimal().size() - 1);
            tableHeader.addCell(new PdfPCell(new Phrase(ultimaPesagem.getDataPesagem().format(DateTimeFormatter.ofPattern("dd/MM/uuuu")) + " : " + ultimaPesagem.getPeso())));
        }

        document.add(tableHeader);
    }

    private static void addCabecalho(Document document) throws DocumentException {
        Font bold = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
        Paragraph paragraph = new Paragraph("Prontuario", bold);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        document.add(paragraph);
    }

    private static BaseColor getCinza() {
        return WebColors.getRGBColor("#57636e");
    }
}
