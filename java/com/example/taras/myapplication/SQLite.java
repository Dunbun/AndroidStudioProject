package com.example.taras.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {

   public static final int DATABASE_VERSION = 1;
   public static final String DATABASE_NAME = "accountsDB";
   public static final String TABLE_NAME = "accounts";

   public static final String KEY_ID = "_id";
   public static final String KEY_EMAIL = "email";
   public static final String KEY_PASSWORD = "password";

    public SQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_EMAIL + " TEXT," +
                KEY_PASSWORD + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



}
