package com.example.hyunwoo.wintercoding;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "WINTER_CODING";
    private final String DATABASE_TABLE = "schedule";
    private final String COLUMN_NUM = "num";
    private final String COLUMN_YEAR = "year";
    private final String COLUMN_MONTH = "month";
    private final String COLUMN_DATE = "date";
    private final String COLUMN_CONTENT = "content";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table if not exists " + DATABASE_TABLE + "(" + COLUMN_NUM + " integer primary key autoincrement, " + COLUMN_YEAR + " integer, "
                + COLUMN_MONTH + " integer, " + COLUMN_DATE + " integer, " + COLUMN_CONTENT + " text)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addSchedule(int year, int month, int date, String content){
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_YEAR, year);
            values.put(COLUMN_MONTH, month);
            values.put(COLUMN_DATE, date);
            values.put(COLUMN_CONTENT, content);
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(DATABASE_TABLE, null, values);
            db.close();
        } catch (Exception e){
            return false;
        }
        return true;
    }

    public ArrayList<Schedule> findSchedule(int year, int month, int date){
        try {
            String query = "select * from " + DATABASE_TABLE + " where " + COLUMN_YEAR + "=" + year + " and "
                    + COLUMN_MONTH + "=" + month + " and " + COLUMN_DATE + "=" + date;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            ArrayList<Schedule> ret = new ArrayList<>();
            while (cursor.moveToNext()) {
                ret.add(new Schedule(cursor.getInt(1),
                        cursor.getInt(2), cursor.getInt(3), cursor.getString(4)));
            }
            db.close();
            return ret;
        }catch(Exception e){
            return null;
        }
    }

    public boolean deleteSchedule(int year, int month, int date, String content){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(DATABASE_TABLE, COLUMN_YEAR + "=" + year + " and " + COLUMN_MONTH + "=" + month + " and "
                    + COLUMN_DATE + "=" + date + " and " + COLUMN_CONTENT + "='" + content + "'", null);
            db.close();
        } catch (Exception e){
            return false;
        }
        return true;
    }
}
