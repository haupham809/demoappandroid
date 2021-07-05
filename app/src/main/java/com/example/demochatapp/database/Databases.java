package com.example.demochatapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Databases extends SQLiteOpenHelper {


    public Databases(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public  void querydata(String sql){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);

    }
    public Cursor getdata(String sql){

        SQLiteDatabase db=getReadableDatabase();
        return  db.rawQuery(sql,null);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
