package com.websarva.wings.android.homono_ap05;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HMG001.db";
    private  static  final int DATABASE_VERSION = 2;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //テーブル設計書そのままひっぱってきた
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE HMG001(");
        sb.append("_id INTEGER PRIMARY KEY,");
        sb.append("store_name TEXT,");
        sb.append("place_id TEXT,");
        sb.append("lat REAL,");
        sb.append("lng REAL");
        sb.append(");");
        String create_sql = sb.toString();

        db.execSQL(create_sql);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

        String sql2 = "DROP TABLE IF EXISTS HMG001;";
        db.execSQL(sql2);

        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db,int oldVersion,int newVersion){
        onUpgrade(db,oldVersion,newVersion);
    }
}
