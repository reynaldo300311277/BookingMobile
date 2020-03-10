package com.example.bookingmobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;

public class CHotelsFromCity
{
    private SQLiteDatabase mDatabase;
    private CHotel hotel;
    private CHotelFacility[] hotelFacilities;
    private CSightseeing[] hotelSightseeing;
    private CRoom[] roomsHotel;

    private String city;
    private String dateCheckIn;
    private String dateCheckOut;
    private int numRooms;
    private int numAdults;
    private int numChildren;

    private Boolean[] filters = new Boolean[8];

    public CHotelsFromCity(SQLiteDatabase mDatabase)
    {
        this.mDatabase = mDatabase;
    }

    public CHotelsFromCity(SQLiteDatabase mDatabase, String city, String dateCheckIn, String dateCheckOut,
                           int numRooms, int numAdults, int numChildren)
    {
        this.mDatabase = mDatabase;
        this.city = city;

        this.dateCheckIn = dateCheckIn;
        this.dateCheckOut = dateCheckOut;
        this.numRooms = numRooms;
        this.numAdults = numAdults;
        this.numChildren = numChildren;

        filters[0] = true;
        filters[1] = true;
        filters[2] = true;
        filters[3] = true;
        filters[4] = true;
        filters[5] = true;
        filters[6] = true;
        filters[7] = true;
    }

    // [0] Parking          [1] Double Bed  [2] Free WiFi       [3] Smoking Free
    // [4] Pet Friendly     [5] Gym         [6] Swimming Pool   [7] Sauna
    public void setFilters(boolean hasParking, boolean hasDoubleBed, boolean hasFreeWifi,
                           boolean isSmokingFree, boolean isPetFriendly, boolean hasGym,
                           boolean hasSwimmingPool, boolean hasSauna)
    {
        filters[0] = hasParking;
        filters[1] = hasDoubleBed;
        filters[2] = hasFreeWifi;
        filters[3] = isSmokingFree;
        filters[4] = isPetFriendly;
        filters[5] = hasGym;
        filters[6] = hasSwimmingPool;
        filters[7] = hasSauna;
    }

    //public ArrayList<CHotel> getHotelsFromCity()
    public ArrayList<CHotel> getHotelsFromCity()
    {
        ArrayList<CHotel> arrayHotels = new ArrayList<>();

        try
        {
            Cursor cursor;

            String query = "" +
                    "SELECT DISTINCT h.pkHotel, r.pkRoom " +
                    "FROM Hotel AS h " +
                        "INNER JOIN Sightseeing AS s ON h.pkHotel = s.fkHotel " +
                        "INNER JOIN Hotel_HotelFacility AS hhf ON h.pkHotel = hhf.fkHotel " +
                        "INNER JOIN HotelFacility AS hf ON hhf.fkHotelFacility = hf.pkHotelFacility " +
                        "INNER JOIN Room As r ON h.pkHotel = r.fkHotel " +
                        "INNER JOIN Booking_Room AS br ON r.pkRoom = br.fkRoom " +
                        "INNER JOIN Booking AS b ON br.fkBooking = b.pkBooking " +
                    "WHERE " +
                        "h.city = '" + this.city + "' AND " +
                        "r.maxNumAdults <= " + this.numAdults + " AND " +
                        "r.maxNumChildren <= " + this.numChildren + " AND " +
                        "b.dateCheckIn NOT BETWEEN '" + this.dateCheckIn + "' AND '" + this.dateCheckOut + "' AND " +
                        "b.dateCheckOut NOT BETWEEN '" + this.dateCheckIn + "' AND '" + this.dateCheckOut + "' AND " +
                        this.dateCheckIn + " NOT BETWEEN b.dateCheckIn AND b.dateCheckOut AND " +
                        this.dateCheckOut + " NOT BETWEEN b.dateCheckIn AND b.dateCheckOut " +
                    "ORDER BY pkHotel, pkRoom";

            // need including the FILTERS!!!!!

            cursor = mDatabase.rawQuery(query,null);

            if (cursor.getCount() == 0) {
                mDatabase.close();
                return null;
            }

            cursor.moveToFirst();

            // store the pair of indexes
            ArrayList<Integer[]> listIndexes = new ArrayList<>();

            do
            {
                int pkH = cursor.getInt(cursor.getColumnIndex("pkHotel"));
                int pkR = cursor.getInt(cursor.getColumnIndex("pkRoom"));

                Integer[] pairPk = {pkH, pkR};
                listIndexes.add(pairPk);
            }
            while (cursor.moveToNext());

            cursor.close();
            int idxReference = -1;

            // build the CHotel Objects
            for (int i=0; i< listIndexes.size(); i++)
            {
                if (idxReference != listIndexes.get(i)[0])
                {
                    CHotel cHotel = new CHotel(listIndexes.get(i)[0]);

                    String queryHotel = "SELECT name, address, city, province, description, " +
                            "latitude, longitude, phone, email " +
                            "FROM Hotel " +
                            "WHERE pkHotel = '" + listIndexes.get(i)[0] + "' " +
                            "ORDER BY pkHotel";

                    Cursor cursorHotel = mDatabase.rawQuery(queryHotel,null);
                    cursorHotel.moveToFirst();

                    cHotel.setName(cursorHotel.getString(cursorHotel.getColumnIndex("name")));
                    cHotel.setCity(cursorHotel.getString(cursorHotel.getColumnIndex("city")));
                    cHotel.setProvince(cursorHotel.getString(cursorHotel.getColumnIndex("province")));
                    cHotel.setDescription(cursorHotel.getString(cursorHotel.getColumnIndex("description")));
                    cHotel.setLatitude(cursorHotel.getFloat(cursorHotel.getColumnIndex("latitude")));
                    cHotel.setLongitude(cursorHotel.getFloat(cursorHotel.getColumnIndex("longitude")));

                    arrayHotels.add(cHotel);
                    cursorHotel.close();
                    idxReference = listIndexes.get(i)[0];
                }
                else
                {
                    idxReference = listIndexes.get(i)[0];
                }
            }

            // build the CRoom Objects and assign to its CHotel
            for (int i=0; i< listIndexes.size(); i++)
            {
                CRoom cRoom = new CRoom(listIndexes.get(i)[1]);

                String queryRoom = "SELECT price, type, description, maxNumAdults, " +
                        "maxNumChildren " +
                        "FROM Room " +
                        "WHERE pkRoom = '" + listIndexes.get(i)[1] + "' AND " +
                            "fkHotel = '" + listIndexes.get(i)[0] + "' " +
                        "ORDER BY pkRoom";

                Cursor cursorRoom = mDatabase.rawQuery(queryRoom,null);
                cursorRoom.moveToFirst();

                cRoom.setFkHotel(listIndexes.get(i)[0]);
                cRoom.setPrice(cursorRoom.getFloat(cursorRoom.getColumnIndex("price")));
                cRoom.setType(cursorRoom.getString(cursorRoom.getColumnIndex("type")));
                cRoom.setDescription(cursorRoom.getString(cursorRoom.getColumnIndex("description")));
                cRoom.setMaxNumAdults(cursorRoom.getInt(cursorRoom.getColumnIndex("maxNumAdults")));
                cRoom.setMaxNumchildren(cursorRoom.getInt(cursorRoom.getColumnIndex("maxNumChildren")));

                for (int j=0; j<arrayHotels.size(); j++)
                    if (arrayHotels.get(j).getHotel(listIndexes.get(i)[0]) != null)
                        arrayHotels.get(j).getHotel(listIndexes.get(i)[0]).addRoom(cRoom);

                cursorRoom.close();
            }
        }
        catch (Exception e)
        {
        }

        return arrayHotels;
    }

    public int checkUser(String user, String password) {
        Cursor c;
        int total = 0;

        try {
            String query = "SELECT * FROM User " +
                    "WHERE loginName = '" + user + "' AND hashPassword = '" + password + "';";
            c = mDatabase.rawQuery(query,null);

            if (c.getCount() == 0) {
                mDatabase.close();
            }

            total = c.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mDatabase.close();
        return total;
    }
}
