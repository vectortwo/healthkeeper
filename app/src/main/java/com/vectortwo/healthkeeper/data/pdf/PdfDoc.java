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
import com.vectortwo.healthkeeper.data.db.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Generates a PDF document from database data
 */
public class PdfDoc extends BaseInfo<Void, Void> {

    /**
     * A flag indicating to add a Steps page in PDF
     */
    public static final int PAGE_STEPS = 1 << 11;

    /**
     * A flag indicating to add a Medications page in PDF
     */
    public static final int PAGE_DRUG = 1 << 10;

    /**
     * A flag indicating to add a Calories page in PDF
     */
    public static final int PAGE_CALORIE = 1 << 9;

    /**
     * A flag indicating to add a Sleep page in PDF
     */
    public static final int PAGE_SLEEP = 1 << 8;

    /**
     * A flag indicating to add a Water intake page in PDF
     */
    public static final int PAGE_FLUID = 1 << 7;

    /**
     * A flag indicating to add a Weight page in PDF
     */
    public static final int PAGE_WEIGHT = 1 << 6;

    /**
     * A flag indicating to add a Blood Pressure page in PDF
     */
    public static final int PAGE_BLOODPRESSURE = 1 << 5;

    /**
     * A flag indicating to add a Well-being page in PDF
     */
    public static final int PAGE_WELLBEING = 1 << 4;

    /**
     * A flag indicating to add a Blood Sugar page in PDF
     */
    public static final int PAGE_BLOODSUGAR = 1 << 3;

    /**
     * A flag indicating to add a Pulse page in PDF
     */
    public static final int PAGE_PULSE = 1 << 2;

    /**
     * Name of the folder where all generated PDFs are stored
     */
    private static final String STORAGE_FOLDER_NAME = "HealthKeeper";

    private static final String FILE_BASE_NAME = "HPMedicalHistory";

    private Context context;
    private int flags;
    private DateInterval interval;

    /**
     * Represents a single PDF document consisting of pages which are set using {@param flags}
     * parameter. If the data has a date stamp, only those records which are interval.within
     * [minDate; maxDate] interval are included in the PDF.
     * To actually generate a PDF one must call execute() method on the {@link PdfDoc} object.
     * The generation itself is happening in the background via {@link BaseInfo}.
     *
     * @param context
     * @param flags what pages to include in PDF (e.g. {@link #PAGE_BLOODPRESSURE} | {@link #PAGE_FLUID})
     * @param interval date interval according to which the filter should be performed
     */
    public PdfDoc(Context context, int flags, DateInterval interval) {
        this.context = context;
        this.flags = flags;
        this.interval = interval;
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
            PdfWriter.getInstance(document, new FileOutputStream(path));

            document.open();

            addMetaData(document);

            appendPages(document);

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private void addMetaData(Document document) {
        document.addTitle("My medical history");
        document.addSubject("Personal data about my health");
        document.addKeywords("medical, health, healthkeeper");
        document.addAuthor("Health Keeper app");
        document.addCreator("Health Keeper app");
    }

    private void appendPages(Document document) throws DocumentException, IOException {
        new PageTitle().addPage(context, document).newPage();

        if (hasFlag(PAGE_DRUG, flags)) {
            final Cursor c = context.getContentResolver().query(DBContract.Drug.CONTENT_URI, null, null, null, null);

            if (c.getCount() != 0) {
                PdfHelper.addHeader(document, "Medications");

                BaseTable baseTable = new BaseTable(
                        new float[]{1, 2, 2, 2, 2, 4, 4},
                        new String[]{"#", "Name", "Interval, times a day", "Start date", "End date"});
                baseTable.fill(new TableFiller() {
                    @Override
                    public void fill(PdfPTable table) {
                        int n = 1;
                        SimpleDateFormat out = new SimpleDateFormat("dd MMM yy");
                        while (c.moveToNext()) {
                            String name = c.getString(c.getColumnIndex(DBContract.Drug.TITLE));
                            Calendar startDate = DrugColumns.getStartDate(c);
                            Calendar endDate = DrugColumns.getEndDate(c);
                            int timesAday = c.getInt(c.getColumnIndex(DBContract.Drug.TIMES_A_DAY));
                            if (interval.withinDates(startDate.getTime())) {
                                table.addCell(String.valueOf(n));
                                table.addCell(name);
                                table.addCell(String.valueOf(timesAday));
                                table.addCell(out.format(startDate));
                                table.addCell(out.format(endDate));

                                n++;
                            }
                        }
                    }
                });
                c.close();

                document.add(baseTable.getTable());
                document.newPage();
            }
        }
        if (hasFlag(PAGE_BLOODPRESSURE, flags)) {
            final Cursor c = context.getContentResolver().query(DBContract.BloodPressure.CONTENT_URI, null, null, null, null);

            if (c.getCount() != 0) {
                PdfHelper.addHeader(document, "Blood Pressure");

                BaseTable baseTable = new BaseTable(
                        new float[]{1, 2, 2, 4},
                        new String[]{"#", "Systolic", "Diastolic", "Date"});
                baseTable.fill(new TableFiller() {
                    @Override
                    public void fill(PdfPTable table) {
                        int n = 1;
                        SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                        while (c.moveToNext()) {
                            int systolic = c.getInt(c.getColumnIndex(DBContract.BloodPressure.SYSTOLIC));
                            int diastolic = c.getInt(c.getColumnIndex(DBContract.BloodPressure.DIASTOLIC));
                            Calendar date = BloodPressureColumns.getDate(c);
                            if (interval.withinDates(date.getTime())) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(systolic));
                                table.addCell(String.valueOf(diastolic));
                                table.addCell(out.format(date));

                                n++;
                            }
                        }
                    }
                });
                c.close();

                document.add(baseTable.getTable());
                document.newPage();
            }
        }
        if (hasFlag(PAGE_BLOODSUGAR, flags)) {
            final Cursor c = context.getContentResolver().query(DBContract.BloodSugar.CONTENT_URI, null, null, null, null);

            if (c.getCount() != 0) {
                PdfHelper.addHeader(document, "Blood Sugar");

                BaseTable baseTable = new BaseTable(
                        new float[]{1, 2, 2, 4},
                        new String[]{"#", "Sugar Level", "Before-meal", "Date"});
                baseTable.fill(new TableFiller() {
                    @Override
                    public void fill(PdfPTable table) {
                        int n = 1;
                        SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                        while (c.moveToNext()) {
                            int value = c.getInt(c.getColumnIndex(DBContract.BloodSugar.VALUE));
                            int afterFood = c.getInt(c.getColumnIndex(DBContract.BloodSugar.AFTER_FOOD));
                            Calendar date = BloodSugarColumns.getDate(c);
                            if (interval.withinDates(date.getTime())) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(value));
                                table.addCell((afterFood == 0) ? "Before" : "After");
                                table.addCell(out.format(date));

                                n++;
                            }
                        }
                    }
                });
                c.close();

                document.add(baseTable.getTable());
                document.newPage();
            }
        }
        if (hasFlag(PAGE_CALORIE, flags)) {
            final Cursor c = context.getContentResolver().query(DBContract.Calorie.CONTENT_URI, null, null, null, null);

            if (c.getCount() != 0) {
                PdfHelper.addHeader(document, "Calories");

                BaseTable baseTable = new BaseTable(
                        new float[]{1, 2, 2, 4},
                        new String[]{"#", "Burned", "Gained", "Date"});
                baseTable.fill(new TableFiller() {
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
                                if (interval.withinDates(date)) {
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

                document.add(baseTable.getTable());
                document.newPage();
            }
        }
        if (hasFlag(PAGE_FLUID, flags)) {
            final Cursor c = context.getContentResolver().query(
                    DBContract.Fluid.PDF_CONTENT_URI,
                    new String[] {"sum(" + DBContract.Fluid.DRANK + ")", DBContract.Fluid.DATE},
                    null, null, null);

            if (c.getCount() != 0) {
                PdfHelper.addHeader(document, "Water intake");

                BaseTable baseTable = new BaseTable(
                        new float[]{1, 2, 4},
                        new String[]{"#", "Drank", "Date"});
                baseTable.fill(new TableFiller() {
                    @Override
                    public void fill(PdfPTable table) {
                        int n = 1;
                        SimpleDateFormat out = new SimpleDateFormat("dd MMM yy");
                        while (c.moveToNext()) {
                            int value = c.getInt(0);
                            Calendar date = FluidColumns.getDate(c);
                            if (interval.withinDates(date.getTime())) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(value));
                                table.addCell(out.format(date));

                                n++;
                            }
                        }
                    }
                });
                c.close();

                document.add(baseTable.getTable());
                document.newPage();
            }
        }
        if (hasFlag(PAGE_PULSE, flags)) {
            final Cursor c = context.getContentResolver().query(DBContract.Pulse.CONTENT_URI, null, null, null, null);

            if (c.getCount() != 0) {
                PdfHelper.addHeader(document, "Pulse");

                BaseTable baseTable = new BaseTable(
                        new float[]{1, 2, 4},
                        new String[]{"#", "Pulse rate", "Date"});
                baseTable.fill(new TableFiller() {
                    @Override
                    public void fill(PdfPTable table) {
                        int n = 1;
                        SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                        while (c.moveToNext()) {
                            int value = c.getInt(c.getColumnIndex(DBContract.Pulse.VALUE));
                            Calendar date = PulseColumns.getDate(c);
                            if (interval.withinDates(date.getTime())) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(value));
                                table.addCell(out.format(date));

                                n++;
                            }
                        }
                    }
                });
                c.close();

                document.add(baseTable.getTable());
                document.newPage();
            }
        }
        if (hasFlag(PAGE_SLEEP, flags)) {
            final Cursor c = context.getContentResolver().query(DBContract.Sleep.CONTENT_URI, null, null, null, null);

            if (c.getCount() != 0) {
                PdfHelper.addHeader(document, "Sleep");

                BaseTable baseTable = new BaseTable(
                        new float[]{1, 2, 4, 4},
                        new String[]{"#", "Sleep time", "Bedtime", "Woke up"});
                baseTable.fill(new TableFiller() {
                    @Override
                    public void fill(PdfPTable table) {
                        int n = 1;
                        SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                        while (c.moveToNext()) {
                            int sleepTime = c.getInt(c.getColumnIndex(DBContract.Sleep.SLEEP_TIME));
                            Calendar startDate = SleepColumns.getStartDate(c);
                            Calendar endDate = SleepColumns.getEndDate(c);
                            if (interval.withinDates(startDate.getTime())) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(sleepTime));
                                table.addCell(out.format(startDate));
                                table.addCell(out.format(endDate));

                                n++;
                            }
                        }
                    }
                });

                c.close();

                document.add(baseTable.getTable());
                document.newPage();
            }
        }
        if (hasFlag(PAGE_STEPS, flags)) {
            final Cursor c = context.getContentResolver().query(
                    DBContract.Steps.PDF_CONTENT_URI,
                    new String[] {
                            "sum(" + DBContract.Steps.COUNT + ")",
                            "sum(" + DBContract.Steps.WALKING_TIME + ")",
                            DBContract.Steps.DATE},
                    null, null, null);

            if (c.getCount() != 0) {
                PdfHelper.addHeader(document, "Steps");

                BaseTable baseTable = new BaseTable(
                        new float[]{1, 2, 2, 4},
                        new String[]{"#", "Steps walked", "Walk time", "Date"});
                baseTable.fill(new TableFiller() {
                    @Override
                    public void fill(PdfPTable table) {
                        int n = 1;
                        SimpleDateFormat out = new SimpleDateFormat("dd MMM yy");
                        while (c.moveToNext()) {
                            int stepCount = c.getInt(0);
                            int walkTime = c.getInt(1);
                            Calendar date = StepColumns.getDate(c);
                            if (interval.withinDates(date.getTime())) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(stepCount));
                                table.addCell(out.format(walkTime));
                                table.addCell(out.format(date));

                                n++;
                            }
                        }
                    }
                });
                c.close();

                document.add(baseTable.getTable());
                document.newPage();
            }
        }
        if (hasFlag(PAGE_WEIGHT, flags)) {
            final Cursor c = context.getContentResolver().query(DBContract.Weight.CONTENT_URI, null, null, null, null);

            if (c.getCount() != 0) {
                PdfHelper.addHeader(document, "Weight");

                BaseTable baseTable = new BaseTable(
                        new float[]{1, 2, 4},
                        new String[]{"#", "Weight", "Date"});
                baseTable.fill(new TableFiller() {
                    @Override
                    public void fill(PdfPTable table) {
                        int n = 1;
                        SimpleDateFormat out = new SimpleDateFormat("dd MMM yy HH-mm");
                        while (c.moveToNext()) {
                            int value = c.getInt(c.getColumnIndex(DBContract.Weight.VALUE));
                            Calendar date = WeightColumns.getDate(c);
                            if (interval.withinDates(date.getTime())) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(value));
                                table.addCell(out.format(date));

                                n++;
                            }
                        }
                    }
                });
                c.close();

                document.add(baseTable.getTable());
                document.newPage();
            }
        }
        if (hasFlag(PAGE_WELLBEING, flags)) {
            final Cursor c = context.getContentResolver().query(DBContract.WellBeing.CONTENT_URI, null, null, null, null);

            if (c.getCount() != 0) {
                PdfHelper.addHeader(document, "Well-being estimates");

                BaseTable baseTable = new BaseTable(
                        new float[]{1, 2, 4},
                        new String[]{"#", "Rate", "Date"});
                baseTable.fill(new TableFiller() {
                    @Override
                    public void fill(PdfPTable table) {
                        int n = 1;
                        SimpleDateFormat out = new SimpleDateFormat("dd MMM yy");
                        while (c.moveToNext()) {
                            int value = c.getInt(c.getColumnIndex(DBContract.WellBeing.VALUE));
                            Calendar date = WellBeingColumns.getDate(c);
                            if (interval.withinDates(date.getTime())) {
                                table.addCell(String.valueOf(n));
                                table.addCell(String.valueOf(value));
                                table.addCell(out.format(date));

                                n++;
                            }
                        }
                    }
                });
                c.close();

                document.add(baseTable.getTable());
                document.newPage();
            }
        }
    }

    /**
     * Increases month by 1 for accurate SimpleDateFormat parsing
     * @param str date corresponding to "Calendar.YEAR-Calendar.MONTH-..."
     * @return String parsed date with increased month
     */
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