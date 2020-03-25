package com.example.bookingmobile;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class CBookingsPerUser {

    private SQLiteDatabase mDatabase;
    private CUser user;

    public CBookingsPerUser(SQLiteDatabase db, CUser user) {
        this.mDatabase = db;
        this.user = user;
    }

    public ArrayList<CBooking> getBookingsPerUser() {
        // store the set of bookings
        ArrayList<CBooking> listBooking = new ArrayList<>();

        try
        {
            Cursor cursor;

            String query = "" +
                    "SELECT pkBooking, fkuser, dateCheckIn, dateCheckOut, numAdults, numChildren, " +
                        "status, totalFeePerNight, credCardName, credCardType, " +
                        "credCardNumber, credCardExpire, credCardCVC\n" +
                    "FROM User AS u INNER JOIN Booking AS b ON u.pkUser = b.fkUser\n" +
                    "WHERE u.pkUser = '" + this.user.getPkUser() + "' AND b.flagDeleted = 1 " +
                    "ORDER BY pkBooking";

            cursor = mDatabase.rawQuery(query,null);

            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();

            do
            {
                int pkBooking = cursor.getInt(cursor.getColumnIndex("pkBooking"));
                int fkUser = cursor.getInt(cursor.getColumnIndex("fkUser"));
                String dateCheckIn = cursor.getString(cursor.getColumnIndex("dateCheckIn"));
                String dateCheckOut = cursor.getString(cursor.getColumnIndex("dateCheckOut"));

                int numAdults = cursor.getInt(cursor.getColumnIndex("numAdults"));
                int numChildren = cursor.getInt(cursor.getColumnIndex("numChildren"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                Double totalFeePerNight = cursor.getDouble(cursor.getColumnIndex("totalFeePerNight"));

                String credCardName = cursor.getString(cursor.getColumnIndex("credCardName"));
                String credCardType = cursor.getString(cursor.getColumnIndex("credCardType"));
                String credCardNumber = cursor.getString(cursor.getColumnIndex("credCardNumber"));
                String credCardExpire = cursor.getString(cursor.getColumnIndex("credCardExpire"));
                String credCardCVC = cursor.getString(cursor.getColumnIndex("credCardCVC"));

                CBooking booking = new CBooking(pkBooking, fkUser, dateCheckIn, dateCheckOut,
                        numAdults, numChildren, status, totalFeePerNight, credCardName,
                        credCardType, credCardNumber, credCardExpire, credCardCVC);

                listBooking.add(booking);
            }
            while (cursor.moveToNext());

            cursor.close();
        }
        catch (Exception e)
        {
        }

        return listBooking;
    }

    public boolean deleteBooking(int idxBooking) {

        try {
            ContentValues val = new ContentValues();
            val.put("flagDeleted", 0);

            mDatabase.update("Booking", val, "pkBooking = ? ",
                    new String[] {String.valueOf(idxBooking)});
        }
        catch (Exception ex) {
            Log.e("UPDATE", ex.getMessage());
        }

        return true;
    }
}
