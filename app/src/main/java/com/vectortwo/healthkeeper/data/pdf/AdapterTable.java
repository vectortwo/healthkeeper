package com.vectortwo.healthkeeper.data.pdf;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPTable;

/**
 * Created by ilya on 30/04/2017.
 */
class AdapterTable {

    private PdfPTable table;

    AdapterTable(float[] colWidths, String[] colNames) {
        table = new PdfPTable(colWidths);

        table.setWidthPercentage(70);
        table.getDefaultCell().setUseAscender(true);
        table.getDefaultCell().setUseDescender(true);
        Font f = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, GrayColor.GRAYWHITE);
        table.getDefaultCell().setBackgroundColor(new GrayColor(0.75f));
        table.getDefaultCell().setFixedHeight(25);

        for (String name : colNames) {
            table.addCell(name);
        }
    }

    PdfPTable getTable() {
        return table;
    }

    void fill(TableFiller filler) {
        table.getDefaultCell().setBackgroundColor(GrayColor.GRAYWHITE);
        table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

        filler.fill(table);
    }
}