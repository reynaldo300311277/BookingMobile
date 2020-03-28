package com.example.bookingmobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CFacilities {

    public static ArrayList<String> getHotelFacilities(SQLiteDatabase mDatabase) {

        ArrayList<String> hotelFacilities = new ArrayList<>();
        String query = "SELECT facility FROM HotelFacility ORDER BY facility";
        Cursor cursor = mDatabase.rawQuery(query,null);

        if (cursor.getCount() == 0) {
            return hotelFacilities;
        }

        cursor.moveToFirst();

        do
        {
            String city = cursor.getString(cursor.getColumnIndex("facility"));
            hotelFacilities.add(city);
        }
        while (cursor.moveToNext());

        return hotelFacilities;
    }

    public static ArrayList<String> getRoomFacilities(SQLiteDatabase mDatabase) {

        ArrayList<String> roomFacilities = new ArrayList<>();
        String query = "SELECT facility FROM RoomFacility ORDER BY facility";
        Cursor cursor = mDatabase.rawQuery(query,null);

        if (cursor.getCount() == 0) {
            return roomFacilities;
        }

        cursor.moveToFirst();

        do
        {
            String city = cursor.getString(cursor.getColumnIndex("facility"));
            roomFacilities.add(city);
        }
        while (cursor.moveToNext());

        return roomFacilities;
    }
}

