package com.vectortwo.healthkeeper.data.pdf;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.itextpdf.text.*;
import com.vectortwo.healthkeeper.data.db.DBContract;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by ilya on 26/04/2017.
 */
class PageTitle {
    Document addPage(Context cntx, Document document) throws DocumentException, IOException {
        Paragraph preface = new Paragraph();

        // signature
        PdfHelper.addSignature(document);
        PdfHelper.addEmptyLine(preface, 2);

        // icon
        InputStream ims = cntx.getAssets().open("pdf_title_icon.png");
        Bitmap bmp = BitmapFactory.decodeStream(ims);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.setAbsolutePosition(250, 700);
        image.setAlignment(Element.ALIGN_CENTER);
        preface.add(image);

        // title
        PdfHelper.addEmptyLine(preface, 4);
        PdfHelper.addTitle(document, "The Health Keeper Medical History");

        // user info
        PdfHelper.addEmptyLine(preface, 5);
        addUserInfo(cntx, preface);

        document.add(preface);

        return document;
    }

    private void addUserInfo(Context cntx, Paragraph to) {
        Cursor c = cntx.getContentResolver().query(DBContract.User.CONTENT_URI, null, null, null, null);
        c.moveToFirst();

        String firstname = c.getString(c.getColumnIndex(DBContract.User.FIRSTNAME));
        String sex = c.getString(c.getColumnIndex(DBContract.User.SEX));
        String country = c.getString(c.getColumnIndex(DBContract.User.COUNTRY));
        String city = c.getString(c.getColumnIndex(DBContract.User.CITY));
        int age = c.getInt(c.getColumnIndex(DBContract.User.AGE));

        c.close();

        to.add(new Paragraph("User information", Fonts.h2));
        PdfHelper.addEmptyLine(to, 1);

        to.add(new Paragraph("Firstname: " + firstname));
        to.add(new Paragraph("Age: " + age));
        to.add(new Paragraph("Sex: " + sex));
        to.add(new Paragraph("Country: " + country));
        to.add(new Paragraph("City: " + city));
    }
}
