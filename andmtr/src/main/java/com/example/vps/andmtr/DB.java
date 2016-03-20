package com.example.vps.andmtr;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB {
    final String LOG_TAG = "andmtr_mylog";
    private static final String DB_NAME = "mydb";
    private static final int DB_VERSION = 1;

    // Table ITEMS definitions
    public static final String TABLE_ITEMS = "ITEMS";
    public static final String COLUMN_ID = "_id";
    public static final String ITEMS_COLUMN_NAME = "name";
    public static final String ITEMS_COLUMN_ADDR = "addr";

    private static final String DB_CREATE_ITEMS =
            "create table " + TABLE_ITEMS + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    ITEMS_COLUMN_NAME + " text, " +
                    ITEMS_COLUMN_ADDR + " text" +
                    ");";

    // Table TESTS definitions
    public static final String TABLE_TESTS = "TESTS";
    //public static final String COLUMN_ID = "_id";
    public static final String TESTS_COLUMN_NAME = "name";
    public static final String TESTS_COLUMN_TYPE = "type";
    public static final String TESTS_COLUMN_INTERVAL = "interval";
    public static final String TESTS_COLUMN_COUNT = "count";
    public static final String TESTS_COLUMN_RESOLVE = "resolve";

    private static final String DB_CREATE_TESTS =
            "create table " + TABLE_TESTS + " (" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    TESTS_COLUMN_NAME + " text, " +
                    TESTS_COLUMN_TYPE + " text," +
                    TESTS_COLUMN_INTERVAL + " integer, " +
                    TESTS_COLUMN_COUNT + " integer, " +
                    TESTS_COLUMN_RESOLVE + " INTEGER " +
                    ");";

    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        Log.d(LOG_TAG, "db.open");
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы TABLE_ITEMS
    public Cursor getAllData(String DB_TABLE) {
        Log.d(LOG_TAG, "getAllData");
        // query (tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
        return mDB.query(DB_TABLE, null, null, null, null, null, null, null);
        //return mDB.query(DB_TABLE, null, null, null, null, null, "name", null);
    }

    //public Cursor getItemById(String _id) {
    //    Log.d(LOG_TAG, "getItemById");
    //    return  mDB.query(TABLE_ITEMS, null, "_id=?", new String[] {_id}, null, null, null);
    //}

    public String[] getItemById (String _id, String DB_TABLE) {
        Log.d(LOG_TAG, "getItemById");
        String[] returned_data;
        Cursor cur;
        switch (DB_TABLE) {
            case TABLE_ITEMS :
                // return string array: {"item name", "item address"}
                Log.d(LOG_TAG, "Get From Table ITEMS");
                cur = mDB.query(DB_TABLE, null, "_id=?", new String[]{_id}, null, null, "_id");
                cur.moveToFirst();
                String item_name = cur.getString(cur.getColumnIndex(ITEMS_COLUMN_NAME));
                String item_addr = cur.getString(cur.getColumnIndex(ITEMS_COLUMN_ADDR));
                returned_data = new String[] {item_name, item_addr};
                cur.close();
                break;
            case TABLE_TESTS :
                // return string array: {"test name", "test type", "test interval", "test count", "test resolve"}
                Log.d(LOG_TAG, "Get From Table TESTS");
                cur = mDB.query(DB_TABLE, null, "_id=?", new String[]{_id}, null, null, "_id");
                cur.moveToFirst();
                String test_name = cur.getString(cur.getColumnIndex(TESTS_COLUMN_NAME));
                String test_type = cur.getString(cur.getColumnIndex(TESTS_COLUMN_TYPE));
                Integer test_interval = cur.getInt(cur.getColumnIndex(TESTS_COLUMN_INTERVAL));
                Integer test_count = cur.getInt(cur.getColumnIndex(TESTS_COLUMN_COUNT));
                Integer test_resolve = cur.getInt(cur.getColumnIndex(TESTS_COLUMN_RESOLVE));
                returned_data = new String[] {test_name, test_type,  String.valueOf(test_interval), String.valueOf(test_count),String.valueOf(test_resolve)};
                cur.close();
                break;
            default:
                returned_data = new String[] {};
        }

        return returned_data;
    }

    // добавить запись в TABLE_ITEMS
    public boolean addRec(String DB_TABLE, String name, String addr) {
        ContentValues cv = new ContentValues();
        cv.put(ITEMS_COLUMN_ADDR, addr);
        cv.put(ITEMS_COLUMN_NAME, name);
        try { mDB.insert(DB_TABLE, null, cv); }
        catch (Exception e) { cv.clear(); return false; }
        cv.clear();
        return true;
    }

    // добавить запись в TABLE_TESTS
    public boolean addRec(String DB_TABLE, String name, String type, Integer interval, Integer count, Integer resolve) {
        ContentValues cv = new ContentValues();
        cv.put(TESTS_COLUMN_NAME, name);
        cv.put(TESTS_COLUMN_TYPE, type);
        cv.put(TESTS_COLUMN_INTERVAL, interval);
        cv.put(TESTS_COLUMN_COUNT, count);
        cv.put(TESTS_COLUMN_RESOLVE, resolve);
        try { mDB.insert(DB_TABLE, null, cv); }
        catch (Exception e) { cv.clear(); return false; }
        cv.clear();
        return true;
    }

    // изменить запись в TABLE_ITEMS
    public boolean updateRec(String DB_TABLE, Long id, String name, String addr) {
        ContentValues cv = new ContentValues();
        cv.put(ITEMS_COLUMN_NAME, name);
        cv.put(ITEMS_COLUMN_ADDR, addr);
        try  { mDB.update(DB_TABLE, cv, COLUMN_ID  + "=" + String.valueOf(id), null); }
        catch (Exception e) { cv.clear(); return false; }
        cv.clear();
        return true;
    }

    // удалить запись из таблицы
    public boolean delRec(String DB_TABLE, long id) {
        try { mDB.delete(DB_TABLE, COLUMN_ID + " = " + id, null); }
        catch (Exception e) { return false; }
        return true;
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_ITEMS);
            db.execSQL(DB_CREATE_TESTS);

            Log.d(LOG_TAG, "onDBCreate");

            // Set default values to table ITEMS
            ContentValues cv = new ContentValues();
            cv.put(ITEMS_COLUMN_NAME, "localhost");
            cv.put(ITEMS_COLUMN_ADDR, "127.0.0.1");
            db.insert(TABLE_ITEMS, null, cv);
            cv.clear();

            cv.put(ITEMS_COLUMN_NAME, "Google");
            cv.put(ITEMS_COLUMN_ADDR, "8.8.8.8");
            db.insert(TABLE_ITEMS, null, cv);
            cv.clear();

            cv.put(ITEMS_COLUMN_NAME, "Trend-Nsk-Office");
            cv.put(ITEMS_COLUMN_ADDR, "109.202.0.172");
            db.insert(TABLE_ITEMS, null, cv);
            cv.clear();

            for (int i=1; i < 15 ; i++ ) {
                cv.put(ITEMS_COLUMN_NAME, i);
                cv.put(ITEMS_COLUMN_ADDR, i);
                db.insert(TABLE_ITEMS, null, cv);
                cv.clear();
            }
            // Set default values to table TESTS
            cv.put(TESTS_COLUMN_TYPE, "ping");
            cv.put(TESTS_COLUMN_NAME, "Ping");
            cv.put(TESTS_COLUMN_INTERVAL, 1);
            cv.put(TESTS_COLUMN_COUNT, 10);
            cv.put(TESTS_COLUMN_RESOLVE, 0);
            db.insert(TABLE_TESTS, null, cv);
            cv.clear();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
