package com.example.perfectbnb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mylocations";

    public static final String LOCATION_TABLE_NAME = "location";
    public static final String _id = "id";
    public static final String NAME = "name";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("create table " + LOCATION_TABLE_NAME);
        String createNoteTableQuery = "create table " + LOCATION_TABLE_NAME + "( "
                + _id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " VARCHAR(30), "
                + LONGITUDE + " VARCHAR(30), "
                + LATITUDE + " VARCHAR(30)) ";

        db.execSQL(createNoteTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + LOCATION_TABLE_NAME);
        onCreate(db);
    }
    public void insertNote(Location location) {
        System.out.println("Inserting Data on Location Table");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(this.NAME, location.name);
        values.put(this.LONGITUDE, location.longitude);
        values.put(this.LATITUDE, location.latitude);
        db.insert(LOCATION_TABLE_NAME, null, values);
    }

    public ArrayList<Location> getAllNotes() {
        System.out.println("getting Data from Notes");
        String selectQuery = "select * from " + LOCATION_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        final  ArrayList<Location> historyArrayList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            System.out.println(" (HISTORY) GETData inside");
            do {
                int id =  cursor.getInt(cursor.getColumnIndexOrThrow(_id));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
                String longitude = cursor.getString(cursor.getColumnIndexOrThrow(LONGITUDE));
                String latitude = cursor.getString(cursor.getColumnIndexOrThrow(LATITUDE));
                Location location = new Location(id,name,longitude,latitude);

                //System.out.println(location.name);
                historyArrayList.add(location);
               // System.out.println("Data from DataBase :"+cursor.getInt(0)+": " + cursor.getString(1).toString()+" Loc:"+cursor.getString(2).toString()+"-"+cursor.getString(3).toString());
                Log.i("Database",cursor.getInt(0)+": " + cursor.getString(1).toString()+" Loc:"+cursor.getString(2).toString()+"-"+cursor.getString(3).toString());
            } while (cursor.moveToNext());
        }

        return historyArrayList;

    }

}