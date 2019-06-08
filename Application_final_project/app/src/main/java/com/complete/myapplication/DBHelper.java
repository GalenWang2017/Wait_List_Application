package com.complete.myapplication;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private int index =0;
    private static final String _DBname = "waitlist.db";
    private static final int _DBVersion=1;
    private static final String _TableName="waitlist";
    public DBHelper(Context context) {
        super(context, _DBname,null,_DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL = "CREATE TABLE IF NOT EXISTS " + _TableName + "( "+"_TITLE STRING(50) NOT NULL, " + "_CONTENT TEXT," + "_CREATETIME TEXT,_TIMEOUTTIME TEXT"+");";

        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion){
            String sql = "DROP TABLE IF EXISTS waitlist";
            db.execSQL(sql);
        }
    }
}
