package com.joulis1derful.tripscheduler.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.joulis1derful.tripscheduler.util.DbContract.KEY_BUS_ID;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY1_HIGHLIGHT;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY1_ID;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY1_NAME;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY2_HIGHLIGHT;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY2_ID;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_CITY2_NAME;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_DATE_FROM;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_DATE_TO;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_ID;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_INFO;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_INFO_FROM;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_INFO_TO;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_PRICE;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_RESERVATION_COUNT;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_TIME_FROM;
import static com.joulis1derful.tripscheduler.util.DbContract.KEY_TIME_TO;
import static com.joulis1derful.tripscheduler.util.DbContract.TABLE_NAME;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "trips";

    private static DbHelper dbInstance;

    private SQLiteDatabase db;

    public static synchronized DbHelper getDbInstance(Context context) {
        if(dbInstance == null) {
            dbInstance = new DbHelper(context.getApplicationContext());
        }
        return dbInstance;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" + KEY_ID + " integer primary key," +
                    KEY_CITY1_ID + " integer," + KEY_CITY1_HIGHLIGHT + " integer," + KEY_CITY1_NAME +
                    " text," + KEY_CITY2_ID + " integer," + KEY_CITY2_HIGHLIGHT + " integer," +
                    KEY_CITY2_NAME + " text," + KEY_DATE_FROM + " text," +
                    KEY_DATE_TO + " text," + KEY_TIME_FROM + " text," + KEY_TIME_TO + " text," +
                    KEY_INFO_FROM + " text," + KEY_INFO_TO + " text," + KEY_INFO + " text," +
                    KEY_BUS_ID + " integer," + KEY_PRICE + " integer," + KEY_RESERVATION_COUNT +
                    " integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public void writeIntoDb(ContentValues cv) {
        long rows = db.insert(TABLE_NAME, null, cv);
        Log.d("AMOUNT OF ROWS INSERTED", String.valueOf(rows));
    }

    public Cursor getAllData() {
        String buildSQL = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(buildSQL, null);
    }

    public Cursor getDataById(int id) {
        String buildSQL = "SELECT * FROM " + TABLE_NAME + " WHERE _id = " + id;
        return db.rawQuery(buildSQL, null);
    }
}
