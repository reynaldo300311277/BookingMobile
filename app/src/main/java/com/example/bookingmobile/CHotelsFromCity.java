package com.example.bookingmobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CHotelsFromCity
{
    private SQLiteDatabase mDatabase;
    private CHotel hotel;
    private CSightseeing[] hotelSightseeing;
    private CRoom[] roomsHotel;

    private String city;
    private String dateCheckIn;
    private String dateCheckOut;
    private int numRooms;
    private int numAdults;
    private int numChildren;

    private String filters = "";

    public CHotelsFromCity(SQLiteDatabase mDatabase) {
        this.mDatabase = mDatabase;

        this.city = "";
        this.dateCheckIn = "2000-01-01";
        this.dateCheckOut = "2000-01-02";
        this.numRooms = 1;
        this.numAdults = 3;
        this.numChildren = 3;
    }

    public String setFiltersHotels (boolean hasParking, boolean hasDoubleBed, boolean hasFreeWifi,
                           boolean isSmokingFree, boolean isPetFriendly, boolean hasGym,
                           boolean hasSwimmingPool, boolean hasSauna) {
        ArrayList<Integer> listFilters = new ArrayList<>();

        if (hasParking)
            listFilters.add(1);
        if (hasDoubleBed)
            listFilters.add(2);
        if (hasFreeWifi)
            listFilters.add(3);
        if (isSmokingFree)
            listFilters.add(4);
        if (isPetFriendly)
            listFilters.add(5);
        if (hasGym)
            listFilters.add(6);
        if (hasSwimmingPool)
            listFilters.add(7);
        if (hasSauna)
            listFilters.add(8);

        if (listFilters.size() > 0)
            for (int i=0; i< listFilters.size(); i++)
            {
                if (i != listFilters.size() - 1)
                    filters += listFilters.get(i).toString() + ", ";
                else
                    filters += listFilters.get(i).toString() + "";
            }

        return filters;
    }

    //public ArrayList<CHotel> getHotelsFromCity()
    public ArrayList<CHotel> getHotelsFromCity(String city, String dateCheckIn, String dateCheckOut,
                                               int numRooms, int numAdults, int numChildren) {
        ArrayList<CHotel> arrayHotels = new ArrayList<>();

        this.city = city;
        this.dateCheckIn = dateCheckIn;
        this.dateCheckOut = dateCheckOut;
        this.numRooms = numRooms;
        this.numAdults = numAdults;
        this.numChildren = numChildren;

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
                        this.dateCheckOut + " NOT BETWEEN b.dateCheckIn AND b.dateCheckOut AND " +
                        "hhf.fkHotelFacility  IN (" + this.filters + ") " +
                    "ORDER BY pkHotel, pkRoom";

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

                    // search and store the facilities for each hotel
                    String queryFacilitiesHotel = "SELECT DISTINCT pkHotel, pkHotelFacility, facility " +
                            "FROM Hotel AS h " +
                                "INNER JOIN Hotel_HotelFacility AS hhf ON h.pkHotel = hhf.fkHotel " +
                                "INNER JOIN HotelFacility AS hf ON hhf.fkHotelFacility = hf.pkHotelFacility " +
                            "WHERE h.pkHotel = '" + listIndexes.get(i)[0] + "' " +
                            "ORDER BY hf.pkHotelFacility;";

                    Cursor cursorFacilitiesHotel = mDatabase.rawQuery(queryFacilitiesHotel,null);
                    cursorFacilitiesHotel.moveToFirst();

                    ArrayList<String> hotelFacilities = new ArrayList<>();

                    do
                    {
                        hotelFacilities.add(cursorFacilitiesHotel.getString(
                                cursorFacilitiesHotel.getColumnIndex("facility")));
                    }
                    while (cursorFacilitiesHotel.moveToNext());

                    cHotel.setFacilitiesHotel(hotelFacilities);

                    // search and store the sightseeing for each hotel
                    String querySightseeing = "SELECT pkSightseeing, fkHotel, name, distance " +
                            "FROM Sightseeing " +
                            "WHERE fkHotel = '" + listIndexes.get(i)[0] + "' " +
                            "ORDER BY distance;";

                    Cursor cursorSightseeing = mDatabase.rawQuery(querySightseeing,null);
                    cursorSightseeing.moveToFirst();

                    ArrayList<CSightseeing> sightseeing = new ArrayList<>();

                    do
                    {
                        sightseeing.add(new CSightseeing(
                                cursorSightseeing.getInt(cursorSightseeing.getColumnIndex("pkSightseeing")),
                                listIndexes.get(i)[0],
                                cursorSightseeing.getString(cursorSightseeing.getColumnIndex("name")),
                                cursorSightseeing.getFloat(cursorSightseeing.getColumnIndex("distance"))
                        ));
                    }
                    while (cursorSightseeing.moveToNext());

                    cHotel.setSightseeing(sightseeing);

                    arrayHotels.add(cHotel);
                    idxReference = listIndexes.get(i)[0];

                    cursorHotel.close();
                    cursorFacilitiesHotel.close();
                    cursorSightseeing.close();
                }
                else
                {
                    idxReference = listIndexes.get(i)[0];
                }
            }

            // build the CRoom Objects and assign to its CHotel
            for (int i=0; i< listIndexes.size(); i++)
            {
                CRoom cRoom = new CRoom(listIndexes.get(i)[1], listIndexes.get(i)[0]);

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
                    {
                        // search and store the facilities for each room
                        String queryFacilitiesRoom = "SELECT DISTINCT pkRoom, pkRoomFacility, facility " +
                                "FROM Room AS r " +
                                    "INNER JOIN Room_RoomFacility AS rrf ON r.pkRoom = rrf.fkRoom " +
                                    "INNER JOIN RoomFacility AS rf ON rrf.fkRoomFacility = rf.pkRoomFacility " +
                                "WHERE pkRoom = '" + listIndexes.get(i)[1] + "' " +
                                "ORDER BY pkRoomFacility;";

                        Cursor cursorFacilitiesRoom = mDatabase.rawQuery(queryFacilitiesRoom,null);
                        cursorFacilitiesRoom.moveToFirst();

                        ArrayList<String> roomFacilities = new ArrayList<>();

                        do
                        {
                            roomFacilities.add(cursorFacilitiesRoom.getString(
                                    cursorFacilitiesRoom.getColumnIndex("facility")));
                        }
                        while (cursorFacilitiesRoom.moveToNext());

                        cRoom.setFacilitiesRoom(roomFacilities);

                        // search and store the photos for each room
                        String queryPhotos = "SELECT pkPhotos, fkRoom, idxImage " +
                                "FROM Photos " +
                                "WHERE fkRoom = '" + listIndexes.get(i)[1] + "' " +
                                "ORDER BY idxImage;";

                        Cursor cursorPhotos = mDatabase.rawQuery(queryPhotos,null);
                        cursorPhotos.moveToFirst();

                        ArrayList<CPhotos> photos = new ArrayList<>();

                        do
                        {
                            photos.add(new CPhotos(
                                    cursorPhotos.getInt(cursorPhotos.getColumnIndex("pkPhotos")),
                                    listIndexes.get(i)[1],
                                    cursorPhotos.getInt(cursorPhotos.getColumnIndex("idxImage"))
                            ));
                        }
                        while (cursorPhotos.moveToNext());

                        cRoom.setPhotos(photos);

                        arrayHotels.get(j).getHotel(listIndexes.get(i)[0]).addRoom(cRoom);

                        cursorFacilitiesRoom.close();
                        cursorPhotos.close();
                    }

                cursorRoom.close();
            }
        }
        catch (Exception e)
        {
        }

        return arrayHotels;
    }
}
