package com.kh.ui_exam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class SqlHelper extends SQLiteOpenHelper {
    public SqlHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    // table 생성
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // varchar2 적용이 되지 않는다. -> varchar사용
        String sql = "create table tbl_student(" +
                "        sno varchar(8) primary key," +
                "        sname varchar(20) not null," +
                "        syear number(1) not null," +
                "        gender varchar(1) check(gender in('M','F'))," +
                "        major varchar(20) not null," +
                "        score number(3) default 0 check(score between 0 and 100))";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
