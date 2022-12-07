package com.kh.ui_exam;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;

public class StudentDao {



    // insert
    public boolean insertStudent(SqlHelper helper, StudentVo vo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String sql = "insert into tbl_student(sno, sname, syear, gender, major, score)" +
                "     values (?, ?, ?, ?, ?, ?)";
        try {
            SQLiteStatement stmt = db.compileStatement(sql);
            stmt.bindString(1, vo.getSno());
            stmt.bindString(2, vo.getSname());
            stmt.bindLong(3, vo.getSyear());
            stmt.bindString(4, vo.getGender());
            stmt.bindString(5, vo.getMajor());
            stmt.bindLong(6, vo.getScore());
            long count = stmt.executeInsert();
            if (count > 0) {
                return true;
            }

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }

    // showList
    public ArrayList<StudentVo> showData(SqlHelper helper) {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "select * from tbl_student";
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<StudentVo> vec = new ArrayList<>();
        while (cursor.moveToNext()) {
            String sno = cursor.getString(0);
            String sname = cursor.getString(1);
            int syear = cursor.getInt(2);
            String gender = cursor.getString(3);
            String major = cursor.getString(4);
            int score = cursor.getInt(5);
            StudentVo studentVo = new StudentVo(sno, sname, syear, gender, major, score);
            vec.add(studentVo);
        }
        db.close();
        return vec;
    }

    // modify
    public boolean UpdateData(SqlHelper helper, StudentVo vo) {
        SQLiteDatabase db = helper.getWritableDatabase();
        try {
            String sql = "update tbl_student set sname = '" + vo.getSname() + "'," +
                    "           syear = " + vo.getSyear() + "," +
                    "           gender = '" + vo.getGender() + "'," +
                    "           major = '" + vo.getMajor() + "'," +
                    "           score = " + vo.getScore() +
                    " where sno = '" + vo.getSno() + "'";

            SQLiteStatement stmt = db.compileStatement(sql);
            int count = stmt.executeUpdateDelete();
            if (count > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }

    // delete
    public boolean deleteData(SqlHelper helper, String sno) {
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            String sql = "delete from tbl_student where sno = '" + sno + "'";
            SQLiteStatement stmt = db.compileStatement(sql);
            int count = stmt.executeUpdateDelete();
            if (count > 0) {
                return true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return false;
    }
}
