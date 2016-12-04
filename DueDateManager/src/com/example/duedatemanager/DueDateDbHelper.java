package com.example.duedatemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.duedatemanager.DueDateDBContract;

public class DueDateDbHelper extends SQLiteOpenHelper{
	 // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "DueDateDB.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + DueDateDBContract.DueItems.TABLE_NAME + " (" +
        DueDateDBContract.DueItems._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        DueDateDBContract.DueItems.COLUMN_NAME_ITEM + TEXT_TYPE + COMMA_SEP +
        DueDateDBContract.DueItems.COLUMN_NAME_DESC + TEXT_TYPE + COMMA_SEP +
        DueDateDBContract.DueItems.COLUMN_NAME_DATE + TEXT_TYPE+
        // Any other options for the CREATE command
        " )";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + DueDateDBContract.DueItems.TABLE_NAME;

    public DueDateDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
