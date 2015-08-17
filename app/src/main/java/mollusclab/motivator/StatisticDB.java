package mollusclab.motivator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by vofedoseenko on 05.08.2015.
 */
public class StatisticDB extends SQLiteOpenHelper {

    public static final String STATISTIC_DATABASEENAME = "StatisticDB";
    public static final String STATISTIC_TABLENAME = "Statistic";
    public static final String DATE_REPORT = "date_report";
    public static final String SCORE = "score";

    public StatisticDB(Context context) {
        super(context, STATISTIC_DATABASEENAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_DB = "CREATE TABLE " + STATISTIC_TABLENAME + " ("
                + DATE_REPORT + " INTEGER PRIMARY KEY, "
                + SCORE + " INTEGER);";
        sqLiteDatabase.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + STATISTIC_TABLENAME);
        onCreate(sqLiteDatabase);
    }

    public void incrementTodayScore(int i)
    {
        Calendar c = Calendar.getInstance();
        String dateStr = String.valueOf(getStartOfDay(c.getTime(), Calendar.getInstance()).getTime());
        int value = getTodayScore() + i;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SCORE, value);
        int nRowsEffect = db.update(STATISTIC_TABLENAME, cv, DATE_REPORT + " = ?", new String[]{dateStr});
        if(nRowsEffect == 0) {
            cv.put(DATE_REPORT, dateStr);
            db.insert(STATISTIC_TABLENAME, null, cv);
        }
    }

    public void CheckPast() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        String dateStr = String.valueOf(getStartOfDay(c.getTime(), Calendar.getInstance()).getTime());
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(STATISTIC_TABLENAME, new String[]{SCORE}, DATE_REPORT + " = ?", new String[]{dateStr}, null, null, null);
        cursor.moveToFirst();
        while (cursor.getCount() == 0)
        {
            ContentValues cv = new ContentValues();
            cv.put(SCORE, 0);
            cv.put(DATE_REPORT, dateStr);
            db.insert(STATISTIC_TABLENAME, null, cv);

            c.add(Calendar.DATE, -1);
            dateStr = String.valueOf(getStartOfDay(c.getTime(), Calendar.getInstance()).getTime());
            cursor = db.query(STATISTIC_TABLENAME, new String[]{SCORE}, DATE_REPORT + " = ?", new String[]{dateStr}, null, null, null);
            cursor.moveToFirst();
        }
    }

    public int getTodayScore()
    {
        Calendar c = Calendar.getInstance();
        String dateStr = String.valueOf(getStartOfDay(c.getTime(), Calendar.getInstance()).getTime());
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(STATISTIC_TABLENAME, new String[]{SCORE}, DATE_REPORT + " = ?", new String[]{dateStr}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0)
            return 0;
        return cursor.getInt(0);
    }

    public HashMap<Long, Integer> getListData()
    {
        Calendar c = Calendar.getInstance();
        Date to = c.getTime();
        c.add(Calendar.DATE, -30);
        Date from = c.getTime();
        String toStr = String.valueOf(getStartOfDay(to, Calendar.getInstance()).getTime());
        String fromStr = String.valueOf(getStartOfDay(from, Calendar.getInstance()).getTime());
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(STATISTIC_TABLENAME, new String[]{DATE_REPORT, SCORE}, DATE_REPORT + " >= ? AND " + DATE_REPORT + " <= ?", new String[]{fromStr, toStr}, null, null, null);
        HashMap<Long, Integer> result = new HashMap<>();
        while (cursor.moveToNext()){
           result.put(cursor.getLong(0), cursor.getInt(1));
        }
        return result;
    }
    public static Date getStartOfDay(Date day, Calendar cal) {
        if (day == null)
            day = new Date();
        cal.setTime(day);
        cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
        cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
        return cal.getTime();
    }

    public int getRecordMonthScore() {
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("MM.yyyy");
        String dateStr = df.format(c.getTime());
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(score) As sum_score,  strftime(\"%m.%Y\", datetime(date_report/1000, 'unixepoch')) AS month_report FROM Statistic WHERE month_report <> ? GROUP BY month_report ORDER BY sum_score DESC", new String[]{dateStr});
        cursor.moveToFirst();
        if (cursor.getCount() == 0)
            return 0;
        return cursor.getInt(0);
    }

    public int getRecordDayScore() {

        Calendar c = Calendar.getInstance();
        String dateStr = String.valueOf(getStartOfDay(c.getTime(), Calendar.getInstance()).getTime());
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(STATISTIC_TABLENAME, new String[]{"MAX(" + SCORE + ")"}, DATE_REPORT + " <> ?", new String[]{dateStr}, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() == 0)
            return 0;
        return cursor.getInt(0);
    }

    public int getMonthScore() {
        Calendar c = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("MM.yyyy");
        String dateStr = df.format(c.getTime());
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(score) As sum_score,  strftime(\"%m.%Y\", datetime(date_report/1000, 'unixepoch')) AS month_report FROM Statistic WHERE month_report = ? GROUP BY month_report ORDER BY sum_score DESC", new String[]{dateStr});
        cursor.moveToFirst();
        if (cursor.getCount() == 0)
            return 0;
        return cursor.getInt(0);
    }
}
