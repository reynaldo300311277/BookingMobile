package com.example.bookingmobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CDestinations {

    public static ArrayList<String> getDestinations(SQLiteDatabase mDatabase) {

        ArrayList<String> cities = new ArrayList<>();
        String query = "SELECT DISTINCT city FROM Hotel ORDER BY city;";
        Cursor cursor = mDatabase.rawQuery(query,null);

        if (cursor.getCount() == 0) {
            return cities;
        }

        cursor.moveToFirst();

        do
        {
            String city = cursor.getString(cursor.getColumnIndex("city"));
            cities.add(city);
        }
        while (cursor.moveToNext());

        return cities;
    }
}
