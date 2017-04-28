package com.vectortwo.healthkeeper.data.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by ilya on 26/04/2017.
 */
public interface PageBase {
    void addPage(Document to, PdfWriter writer) throws DocumentException;
}
