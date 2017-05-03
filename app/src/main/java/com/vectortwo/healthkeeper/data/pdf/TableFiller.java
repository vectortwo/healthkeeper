package com.vectortwo.healthkeeper.data.pdf;

import com.itextpdf.text.pdf.PdfPTable;

/**
 * Created by ilya on 04/05/2017.
 */
interface TableFiller {
    void fill(PdfPTable table);
}
