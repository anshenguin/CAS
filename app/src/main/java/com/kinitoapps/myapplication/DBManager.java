package com.kinitoapps.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, int id, int amount, int money) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.NAME, name);
        contentValue.put(DatabaseHelper.FOOD_ID, id);
        contentValue.put(DatabaseHelper.AMOUNT, amount);
        contentValue.put(DatabaseHelper.MONEY, money);

        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.NAME, DatabaseHelper.FOOD_ID, DatabaseHelper.AMOUNT,DatabaseHelper.MONEY };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int doesExist(int id){
        Cursor cursor = database.rawQuery("SELECT * FROM CART WHERE food_id = "+id,null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public int countAmount(int id){
        int amount = 0;
        Cursor cur= database.rawQuery("SELECT amount FROM CART WHERE food_id = "+id,null);
        if(cur.moveToFirst())
        {
            amount = (cur.getInt(0));
            cur.close();
        }
        return amount;

    }

    public int countAll(){
        Cursor cursor= database.rawQuery("SELECT * FROM CART",null);
        int amount=0;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                amount++;
                cursor.moveToNext();
            }
        }

        return amount;

    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.FOOD_ID + "=" + _id, null);
    }

    public int update(int amount,int foodid) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.AMOUNT, amount);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper.FOOD_ID + " = " + foodid, null);
        return i;
    }


}