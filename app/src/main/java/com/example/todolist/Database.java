package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "TODOLIST";
    private static final String TABLE_NAME ="TASK";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String DETAIL = "detail";
    private static final String TIME = "time";
    private static final String DATE = "date";
    private static final String CURRENTDATE_TIME = "currentDate_time";


    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuery = "CREATE TABLE "+TABLE_NAME+" ("
                + ID + " INTEGER PRIMARY KEY NOT NULL,"
                +NAME+" TEXT,"
                +DETAIL +" TEXT,"
                +TIME + " TEXT ,"
                +DATE +" TEXT,"
                +CURRENTDATE_TIME +" TEXT)";

        sqLiteDatabase.execSQL(sqlQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void insert(Integer id,String name,String detail,String time,String date,String currentDate_time){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID,id);
        contentValues.put(NAME,name);
        contentValues.put(DETAIL,detail);
        contentValues.put(TIME,time);
        contentValues.put(DATE,date);
        contentValues.put(CURRENTDATE_TIME,currentDate_time);

        database.insert(TABLE_NAME,null,contentValues);
        database.close();
    }
    public void update(Integer id,String name,String detail,String time,String date,String currentDate_time){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME,name);
        contentValues.put(DETAIL,detail);
        contentValues.put(TIME,time);
        contentValues.put(DATE,date);
        contentValues.put(CURRENTDATE_TIME,currentDate_time);
        database.update(TABLE_NAME,contentValues,"id = ?",new String[] {Integer.toString(id)});
    }

    public Cursor getTask(int id){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID+" = "+id,null);
        return cursor;
    }
    public ArrayList<ToDoList> select(){
        ArrayList<ToDoList> arrayList = new ArrayList<ToDoList>();
        String sqlSelect = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(sqlSelect,null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false){
            arrayList.add(new ToDoList(cursor.getInt(0), cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5) ));
            cursor.moveToNext();
        }
        return arrayList;
    }
    public Integer delete(Integer id){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TABLE_NAME,"id = ?",new String[]{Integer.toString(id)});
    }
}
