package com.vectortwo.healthkeeper.data.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;

import java.util.Calendar;

/**
 * Helper class containing frequently used PDF-related functions
 */ 
class PdfHelper {
    static void addTitle(Document to, String title) throws DocumentException {
        Paragraph paragraph = new Paragraph(title, Fonts.h1);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        to.add(paragraph);
    }

    static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    static void addEmptyLine(Document document, int number) throws DocumentException {
        for (int i = 0; i < number; i++) {
            document.add(new Paragraph(" "));
        }
    }

    static void addSignature(Document document) throws DocumentException {
        Calendar cal = Calendar.getInstance();
        Paragraph signature = new Paragraph("auto generated on " + cal.getTime().toString(), Fonts.signatureFont);
        signature.setAlignment(Element.ALIGN_RIGHT);

        document.add(signature);
    }

    static void addHeader(Document document, String title) throws DocumentException {
        PdfHelper.addSignature(document);
        PdfHelper.addTitle(document, title);
        PdfHelper.addEmptyLine(document, 1);
    }
}
