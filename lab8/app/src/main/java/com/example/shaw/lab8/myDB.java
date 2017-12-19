package com.example.shaw.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Shaw on 2017/12/18.
 */

public class myDB extends SQLiteOpenHelper {
    private static final String DB_NAME = "Contacts.db";
    private static final String TABLE_NAME = "Contacts";
    private static final int DB_VERSION = 1;
//    public myDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//    }
    public myDB(Context context){
        super(context, DB_NAME, null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "create table " + TABLE_NAME
                + "(_id integer primary key, "
                +"name text, "
                +"birth text, "
                +"gift text);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //No need to do
    }

    public int insert(String name, String birth, String gift){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer temp_id = -1;
        values.put("name", name);
        values.put("birth", birth);
        values.put("gift", gift);
        db.insert(TABLE_NAME, null, values);
        Cursor cursor = db.rawQuery("select last_insert_rowid() from "+ TABLE_NAME, null );
        if(cursor.moveToFirst()){
            temp_id = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return temp_id;
    }

    public void update(Integer id, String name, String birth, String gift){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        String whereClause = "_id = ?";
        String[] whereArgs = {id.toString()};
        values.put("name", name);
        values.put("birth", birth);
        values.put("gift", gift);
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public void delete(Integer id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArgs = {id.toString()};
        db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    //将查询内容转换成ArrayList类型
    private ArrayList<Map<String, Object>> convert(Cursor cursor){
        ArrayList<Map<String, Object>> temp = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                Map<String, Object> tempMap = new LinkedHashMap<>();
                tempMap.put("id", cursor.getInt(cursor.getColumnIndex("_id")));
                tempMap.put("name", cursor.getString(cursor.getColumnIndex("name")));
                tempMap.put("birthday", cursor.getString(cursor.getColumnIndex("birth")));
                tempMap.put("gift", cursor.getString(cursor.getColumnIndex("gift")));
                temp.add(tempMap);

            }while(cursor.moveToNext());
        }
        return temp;
    }

    //执行全部查询
    public ArrayList<Map<String, Object>> query_all(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        ArrayList<Map<String, Object>> temp = convert(cursor);
        cursor.close();
        db.close();
        return temp;
    }

    //执行单条按值查询
    public Map<String, Object> query(Integer id){
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "_id = ?";
        String[] whereArgs = {id.toString()};
        Cursor cursor = db.query(TABLE_NAME, new String[]{"_id", "name", "birth", "gift"},
                whereClause, whereArgs, null, null, null);
        ArrayList<Map<String, Object>> temp = convert(cursor);
        Map<String, Object> res = temp.get(0);
        cursor.close();
        db.close();
        return res;
    }

    //判断是否已经有存在某姓名的条目
    public boolean is_name_exist(String name){
        boolean res = false;
        SQLiteDatabase db = getReadableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = {name};
        Cursor cursor = db.query(TABLE_NAME, new String[]{"name"}, whereClause, whereArgs, null, null, null);
        if(cursor.getCount() > 0){
            res = true;
        }
        return  res;
    }
}
