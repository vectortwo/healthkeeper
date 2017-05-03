package com.vectortwo.healthkeeper.data.pdf;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.widget.Toast;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vectortwo.healthkeeper.data.BaseInfo;
import com.vectortwo.healthkeeper.data.db.DBContract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ilya on 26/04/2017.
 */
public class PdfDoc extends BaseInfo<Void, Void> {
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

    private Context context;
    private int flags;
    private Calendar minDate, maxDate;

    public PdfDoc(Context context, int flags, Calendar minDate, Calendar maxDate) {
        this.context = context;
        this.flags = flags;
        this.minDate = minDate;
        this.maxDate = maxDate;

        minDate.add(Calendar.DAY_OF_MONTH, -1);
        maxDate.add(Calendar.DAY_OF_MONTH, 1);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        generate();
        return null;
    }

    private void generate() {
        if (isExternalStorageWritable()) {
            File folder = new File(Environment.getExternalStorageDirectory(), STORAGE_FOLDER_NAME);
            folder.mkdirs();
            if (folder.exists()) {
                Calendar cal = Calendar.getInstance();
                String fileName = getFileName(cal);

                String filePath = folder.getPath() + "/" + fileName + ".pdf";
                createPDF(filePath);
            }
        } else {
            final String msg = "Error! Media is not present";
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    private void createPDF(String path) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            addMetaData(document);
            appendPages(document, writer);
            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private boolean withinDates(Date date) {
        return date.after(minDate.getTime()) && date.before(maxDate.getTime());
    }

    private void addMetaData(Document document) {
        document.addTitle("My medical history");
        document.addSubject("Personal data about my health");
        document.addKeywords("medical, health, healthkeeper");
        document.addAuthor("Health Keeper app");
        document.addCreator("Health Keeper app");
    }

    private void appendPages(Document document, PdfWriter writer) throws DocumentException, IOException {
        new PageTitle().addPage(context, document);

        if (hasFlag(PAGE_DRUG, flags)) {

        }
        if (hasFlag(PAGE_BLOODPRESSURE, flags)) {
            PdfHelper.addHeader(document, "Blood Pressure");

            final Cursor c = context.getContentResolver().query(DBContract.BloodPressure.CONTENT_URI, null, null, null, null);
            AdapterTable adapter = new AdapterTable(
                    new float[] {1, 2, 2, 4},
                    new String[] {"#", "Systolic", "Diastolic", "Date"});
            adapter.fill(new TableFiller() {
                @Override
                public void fill(PdfPTable table) {
                    int n = 1;
                    SimpleDateFormat in = new SimpleDateFormat("y-M-d-H-m");
                    SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                    while (c.moveToNext()) {
                        int systolic = c.getInt(c.getColumnIndex(DBContract.BloodPressure.SYSTOLIC));
                        int diastolic = c.getInt(c.getColumnIndex(DBContract.BloodPressure.DIASTOLIC));
                        String dateStr = c.getString(c.getColumnIndex(DBContract.BloodPressure.DATE));
                        dateStr = fixMonth(dateStr);
                        try {
                            Date date = in.parse(dateStr);
                            if (withinDates(date)) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(systolic));
                                table.addCell(String.valueOf(diastolic));
                                table.addCell(out.format(date));

                                n++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            c.close();

            document.add(adapter.getTable());
            document.newPage();
        }
        if (hasFlag(PAGE_BLOODSUGAR, flags)) {
            PdfHelper.addHeader(document, "Blood Sugar");

            final Cursor c = context.getContentResolver().query(DBContract.BloodSugar.CONTENT_URI, null, null, null, null);
            AdapterTable adapter = new AdapterTable(
                    new float[] {1, 2, 2, 4},
                    new String[] {"#", "Sugar Level", "Before-meal", "Date"});
            adapter.fill(new TableFiller() {
                @Override
                public void fill(PdfPTable table) {
                    int n = 1;
                    SimpleDateFormat in = new SimpleDateFormat("y-M-d-H-m");
                    SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                    while (c.moveToNext()) {
                        int value = c.getInt(c.getColumnIndex(DBContract.BloodSugar.VALUE));
                        int afterFood = c.getInt(c.getColumnIndex(DBContract.BloodSugar.AFTER_FOOD));
                        String dateStr = c.getString(c.getColumnIndex(DBContract.BloodSugar.DATE));
                        dateStr = fixMonth(dateStr);
                        try {
                            Date date = in.parse(dateStr);
                            if (withinDates(date)) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(value));
                                table.addCell((afterFood == 0) ? "Before" : "After");
                                table.addCell(out.format(date));

                                n++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            c.close();

            document.add(adapter.getTable());
            document.newPage();
        }
        // todo: daily?
        if (hasFlag(PAGE_CALORIE, flags)) {
            PdfHelper.addHeader(document, "Calories");

            final Cursor c = context.getContentResolver().query(DBContract.Calorie.CONTENT_URI, null, null, null, null);
            AdapterTable adapter = new AdapterTable(
                    new float[] {1, 2, 2, 4},
                    new String[] {"#", "Burned", "Gained", "Date"});
            adapter.fill(new TableFiller() {
                @Override
                public void fill(PdfPTable table) {
                    int n = 1;
                    SimpleDateFormat in = new SimpleDateFormat("y-M-d-H-m");
                    SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                    while (c.moveToNext()) {
                        int burned = c.getInt(c.getColumnIndex(DBContract.Calorie.BURNED));
                        int gained = c.getInt(c.getColumnIndex(DBContract.Calorie.GAINED));
                        String dateStr = c.getString(c.getColumnIndex(DBContract.Calorie.DATE));
                        dateStr = fixMonth(dateStr);
                        try {
                            Date date = in.parse(dateStr);
                            if (withinDates(date)) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(burned));
                                table.addCell(String.valueOf(gained));
                                table.addCell(out.format(date));

                                n++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            c.close();

            document.add(adapter.getTable());
            document.newPage();
        }
        // todo: daily?
        if (hasFlag(PAGE_FLUID, flags)) {
            PdfHelper.addHeader(document, "Water intake");

            final Cursor c = context.getContentResolver().query(DBContract.Fluid.CONTENT_URI, null, null, null, null);
            AdapterTable adapter = new AdapterTable(
                    new float[] {1, 2, 4},
                    new String[] {"#", "Drank", "Date"});
            adapter.fill(new TableFiller() {
                @Override
                public void fill(PdfPTable table) {
                    int n = 1;
                    SimpleDateFormat in = new SimpleDateFormat("y-M-d-H-m");
                    SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                    while (c.moveToNext()) {
                        int value = c.getInt(c.getColumnIndex(DBContract.Fluid.DRANK));
                        String dateStr = c.getString(c.getColumnIndex(DBContract.Fluid.DATE));
                        dateStr = fixMonth(dateStr);
                        try {
                            Date date = in.parse(dateStr);
                            if (withinDates(date)) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(value));
                                table.addCell(out.format(date));

                                n++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            c.close();

            document.add(adapter.getTable());
            document.newPage();
        }
        if (hasFlag(PAGE_PULSE, flags)) {
            PdfHelper.addHeader(document, "Pulse");

            final Cursor c = context.getContentResolver().query(DBContract.Pulse.CONTENT_URI, null, null, null, null);
            AdapterTable adapter = new AdapterTable(
                    new float[] {1, 2, 4},
                    new String[] {"#", "Pulse rate", "Date"});
            adapter.fill(new TableFiller() {
                @Override
                public void fill(PdfPTable table) {
                    int n = 1;
                    SimpleDateFormat in = new SimpleDateFormat("y-M-d-H-m");
                    SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                    while (c.moveToNext()) {
                        int value = c.getInt(c.getColumnIndex(DBContract.Pulse.VALUE));
                        String dateStr = c.getString(c.getColumnIndex(DBContract.Pulse.DATE));
                        dateStr = fixMonth(dateStr);
                        try {
                            Date date = in.parse(dateStr);
                            if (withinDates(date)) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(value));
                                table.addCell(out.format(date));

                                n++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            c.close();

            document.add(adapter.getTable());
            document.newPage();
        }
        if (hasFlag(PAGE_SLEEP, flags)) {
            PdfHelper.addHeader(document, "Sleep");

            final Cursor c = context.getContentResolver().query(DBContract.Sleep.CONTENT_URI, null, null, null, null);
            AdapterTable adapter = new AdapterTable(
                    new float[] {1, 2, 4, 4},
                    new String[] {"#", "Sleep time", "Bedtime", "Woke up"});
            adapter.fill(new TableFiller() {
                @Override
                public void fill(PdfPTable table) {
                    int n = 1;
                    SimpleDateFormat in = new SimpleDateFormat("y-M-d-H-m");
                    SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                    while (c.moveToNext()) {
                        int sleepTime = c.getInt(c.getColumnIndex(DBContract.Sleep.SLEEP_TIME));
                        String startDateStr = c.getString(c.getColumnIndex(DBContract.Sleep.START_DATE));
                        startDateStr = fixMonth(startDateStr);
                        String endDateStr = c.getString(c.getColumnIndex(DBContract.Sleep.END_DATE));
                        endDateStr = fixMonth(endDateStr);
                        try {
                            Date startDate = in.parse(startDateStr);
                            if (withinDates(startDate)) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(sleepTime));
                                table.addCell(out.format(startDate));
                                table.addCell(out.format(endDateStr));

                                n++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            c.close();

            document.add(adapter.getTable());
            document.newPage();
        }
        if (hasFlag(PAGE_STEPS, flags)) {
            PdfHelper.addHeader(document, "Steps");

            final Cursor c = context.getContentResolver().query(
                    DBContract.Steps.PDF_CONTENT_URI,
                    new String[] {
                            "sum(" + DBContract.Steps.COUNT + ")",
                            "sum(" + DBContract.Steps.WALKING_TIME + ")",
                            DBContract.Steps.DATE},
                    null, null, null);
            AdapterTable adapter = new AdapterTable(
                    new float[] {1, 2, 2, 4},
                    new String[] {"#", "Steps walked", "Walk time", "Date"});
            adapter.fill(new TableFiller() {
                @Override
                public void fill(PdfPTable table) {
                    int n = 1;
                    SimpleDateFormat in = new SimpleDateFormat("y-M-d");
                    SimpleDateFormat out = new SimpleDateFormat("dd MMM yy");
                    while (c.moveToNext()) {
                        int stepCount = c.getInt(0);
                        int walkTime = c.getInt(1);
                        String dateStr = c.getString(c.getColumnIndex(DBContract.Steps.DATE));
                        dateStr = fixMonth(dateStr);
                        try {
                            Date date = in.parse(dateStr);
                            if (withinDates(date)) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(stepCount));
                                table.addCell(out.format(walkTime));
                                table.addCell(out.format(date));

                                n++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            c.close();

            document.add(adapter.getTable());
            document.newPage();
        }
        // todo: daily?
        if (hasFlag(PAGE_WEIGHT, flags)) {
            PdfHelper.addHeader(document, "Weight");

            final Cursor c = context.getContentResolver().query(DBContract.Weight.CONTENT_URI, null, null, null, null);
            AdapterTable adapter = new AdapterTable(
                    new float[] {1, 2, 4},
                    new String[] {"#", "Weight", "Date"});
            adapter.fill(new TableFiller() {
                @Override
                public void fill(PdfPTable table) {
                    int n = 1;
                    SimpleDateFormat in = new SimpleDateFormat("y-M-d-H-m");
                    SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                    while (c.moveToNext()) {
                        int value = c.getInt(c.getColumnIndex(DBContract.Weight.VALUE));
                        String dateStr = c.getString(c.getColumnIndex(DBContract.Weight.DATE));
                        dateStr = fixMonth(dateStr);
                        try {
                            Date date = in.parse(dateStr);
                            if (withinDates(date)) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(value));
                                table.addCell(out.format(date));

                                n++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            c.close();

            document.add(adapter.getTable());
            document.newPage();
        }
        if (hasFlag(PAGE_WELLBEING, flags)) {
            PdfHelper.addHeader(document, "Well-being estimates");

            final Cursor c = context.getContentResolver().query(DBContract.WellBeing.CONTENT_URI, null, null, null, null);
            AdapterTable adapter = new AdapterTable(
                    new float[] {1, 2, 4},
                    new String[] {"#", "Rate", "Date"});
            adapter.fill(new TableFiller() {
                @Override
                public void fill(PdfPTable table) {
                    int n = 1;
                    SimpleDateFormat in = new SimpleDateFormat("y-M-d");
                    SimpleDateFormat out = new SimpleDateFormat("dd MMM yy");
                    while (c.moveToNext()) {
                        int value = c.getInt(c.getColumnIndex(DBContract.WellBeing.VALUE));
                        String dateStr = c.getString(c.getColumnIndex(DBContract.WellBeing.DATE));
                        dateStr = fixMonth(dateStr);
                        try {
                            Date date = in.parse(dateStr);
                            if (withinDates(date)) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(value));
                                table.addCell(out.format(date));

                                n++;
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            c.close();

            document.add(adapter.getTable());
            document.newPage();
        }
    }

    private static String fixMonth(String str) {
        String res;
        String[] comps = str.split("-");
        int fixedMonth = Integer.parseInt(comps[1]) + 1;
        res = comps[0] + "-" + String.valueOf(fixedMonth) + "-";
        for (int i = 2; i < comps.length-1; i++) {
            res += comps[i] + "-";
        }
        res += comps[comps.length-1];

        return res;
    }

    /**
     * Generates unique filename with a date stamp
     *
     * @param cal Calendar to initTable a date stamp from
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

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static boolean hasFlag(int flag, int flags) {
        return (flags & flag) == flag;
    }
}