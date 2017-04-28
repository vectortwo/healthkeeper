package com.vectortwo.healthkeeper.data.pdf;

import android.os.Environment;
import android.util.Log;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * Created by ilya on 26/04/2017.
 */
public class PdfDoc {
    public static final int PAGE_USER           = 1 << 12;
    public static final int PAGE_STEPS          = 1 << 11;
    public static final int PAGE_DRUG           = 1 << 10;
    public static final int PAGE_CALORIE        = 1 << 9;
    public static final int PAGE_SLEEP          = 1 << 8;
    public static final int PAGE_FLUID          = 1 << 7;
    public static final int PAGE_WEIGHT         = 1 << 6;
    public static final int PAGE_BLOODPRESSURE  = 1 << 5;
    public static final int PAGE_WELLBEING      = 1 << 4;
    public static final int PAGE_BLOODSUGAR     = 1 << 3;
    public static final int PAGE_PULSE          = 1 << 2;

    private static final String FILE_BASE_NAME = "HPMedicalHistory";

    private static final String STORAGE_FOLDER_NAME = "HealthKeeper";

    public static void generate(int flags) {
        if (isExternalStorageWritable()) {
            File folder = new File(Environment.getExternalStorageDirectory(), STORAGE_FOLDER_NAME);
            folder.mkdirs();
            if (folder.exists()) {
                Calendar cal = Calendar.getInstance();
                String fileName = getFileName(cal);

                String filePath = folder.getPath() + "/" + fileName + ".pdf";
                createPDF(flags, filePath);
            }
        } else {
            Log.e("vectortwo", "Media is not present");
        }
    }

    /**
     * Generates unique filename with a date stamp
     *
     * @param cal Calendar to get a date stamp from
     * @return filename String
     */
    private static String getFileName(Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        int am_pm = cal.get(Calendar.AM_PM);

        String hourf = (hour < 10) ? ("0" + hour) : String.valueOf(hour);
        String minutef = (minute < 10) ? ("0" + minute) : String.valueOf(minute);
        String secondf = (second < 10) ? ("0" + second) : String.valueOf(second);
        String amPm = (am_pm == Calendar.AM) ? "AM" : "PM";

        return FILE_BASE_NAME +
                "-" + month +
                "-" + day +
                "-" + year +
                "-" + hourf +
                ":" + minutef +
                ":" + secondf +
                amPm;
    }

    private static void createPDF(int flags, String path) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            addMetaData(document);
            appendPages(flags, document, writer);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("My medical history");
        document.addSubject("Personal data about my health");
        document.addKeywords("medical, health, healthkeeper");
        document.addAuthor("Health Keeper app");
        document.addCreator("Health Keeper app");
    }

    private static boolean hasFlag(int flag, int flags) {
        return (flags & flag) == flag;
    }

    private static void appendPages(int flags, Document document, PdfWriter writer) throws DocumentException {
        new PageTitle().addPage(document, writer);

        if (hasFlag(PAGE_USER, flags)) {
            new PageUser().addPage(document, writer);
        }
        if (hasFlag(PAGE_DRUG, flags)) {

        }
        if (hasFlag(PAGE_BLOODPRESSURE, flags)) {

        }
        if (hasFlag(PAGE_BLOODSUGAR, flags)) {

        }
        if (hasFlag(PAGE_CALORIE, flags)) {

        }
        if (hasFlag(PAGE_FLUID, flags)) {

        }
        if (hasFlag(PAGE_PULSE, flags)) {

        }
        if (hasFlag(PAGE_SLEEP, flags)) {

        }
        if (hasFlag(PAGE_STEPS, flags)) {

        }
        if (hasFlag(PAGE_WEIGHT, flags)) {

        }
        if (hasFlag(PAGE_WELLBEING, flags)) {

        }
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
