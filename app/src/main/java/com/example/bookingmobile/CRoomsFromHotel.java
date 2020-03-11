package com.example.bookingmobile;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class CRoomsFromHotel {

    private SQLiteDatabase mDatabase;
    private int pkHotel;

    public CRoomsFromHotel(SQLiteDatabase mDatabase, int pkHotel) {
        this.mDatabase = mDatabase;
        this.pkHotel = pkHotel;
    }

/*
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
*/

    // ****************************************
    // tudo é similiar à classe CHotelsFromCity
    // ****************************************

    // construir o método para realizar a entrada dos filtros
    //public String setFiltersHotels (boolean hasParking, boolean hasDoubleBed, boolean hasFreeWifi,
    //                                boolean isSmokingFree, boolean isPetFriendly, boolean hasGym,
    //                                boolean hasSwimmingPool, boolean hasSauna)

    // construir o método para realizar o retorno dos quartos que estejam de acordo com os filtros
    //public ArrayList<CHotel> getHotelsFromCity(String city, String dateCheckIn, String dateCheckOut,
    //                                           int numRooms, int numAdults, int numChildren)

/*
    SELECT DISTINCT r.pkRoom, r.fkHotel, p.pkPhotos, pkBooking
    FROM Room AS r
    INNER JOIN Booking_Room AS br ON r.pkRoom = br.fkRoom
    INNER JOIN Photos AS p ON r.pkRoom = p.fkRoom
    INNER JOIN Room_RoomFacility AS rrf ON r.pkRoom = rrf.fkRoom
    INNER JOIN RoomFacility AS rf ON rrf.fkRoomFacility = rf.pkRoomFacility
    INNER JOIN Booking AS b ON b.pkBooking = br.fkBooking
            WHERE
    r.fkHotel = '11'
*/
}
