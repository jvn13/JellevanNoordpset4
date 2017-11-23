package com.example.jelle.jellevannoord_pset4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDatabase extends SQLiteOpenHelper {

    private static TodoDatabase instance;
    private String[] titles = {"Do laundry","Get started","Finish project"};

    public static TodoDatabase getInstance(Context context) {
        if(instance == null) {
            instance = new TodoDatabase(context, "pset4", null, 1);
        }
        return instance;
    }

    private TodoDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS todos ( _id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, completed INTEGER)");
        for(int i=0; i<titles.length; i++){
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", titles[i]);
            contentValues.put("completed",0);
            sqLiteDatabase.insert("todos", null, contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS todos");
        onCreate(sqLiteDatabase);
    }

    public Cursor selectAll() {
        SQLiteDatabase db = getWritableDatabase();
        return db.rawQuery("SELECT * FROM todos ORDER BY completed", null);
    }


    public void insert(String title, int completed) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("completed",completed);
        db.insert("todos", null, contentValues);
    }

    public void update(String id, int completed) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("completed",completed);
        db.update("todos",contentValues,"_id = ?", new String[]{id});
    }

    public void delete(String id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("todos", "_id = ?", new String[]{id});
    }
}
