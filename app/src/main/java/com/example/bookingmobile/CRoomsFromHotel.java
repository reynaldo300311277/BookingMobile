package com.example.bookingmobile;

import java.util.ArrayList;

public class CRoomsFromHotel
{
    private CHotel hotel;
    private ArrayList<String> listFilters = new ArrayList<>();

    public CRoomsFromHotel(CHotel hotel) {
        this.hotel = hotel;

        // all filters are selected
        this.listFilters.add("Jacuzzi");
        this.listFilters.add("Air Conditioning");
        this.listFilters.add("Locker");
        this.listFilters.add("Coffee Machine");
        this.listFilters.add("Flat Screen TV");
        this.listFilters.add("Mini Fridge");
    }

    public String getHotelName() {
        return hotel.getName();
    }

    public void setFiltersRooms (boolean hasJacuzzi, boolean hasAirConditioning, boolean hasLocker,
                                    boolean hasCoffeeMachine, boolean hasFlatScreenTV, boolean hasMiniFridge) {
        // clear all filters
        listFilters.clear();

        if (hasJacuzzi)
            listFilters.add("Jacuzzi");
        if (hasAirConditioning)
            listFilters.add("Air Conditioning");
        if (hasLocker)
            listFilters.add("Locker");
        if (hasCoffeeMachine)
            listFilters.add("Coffee Machine");
        if (hasFlatScreenTV)
            listFilters.add("Flat Screen TV");
        if (hasMiniFridge)
            listFilters.add("Mini Fridge");
    }

    private boolean checkRoomHasFacility(ArrayList<String> lFacilities, ArrayList<String> lFilters) {

        for (int j=0; j< lFacilities.size(); j++)
            for (int k=0; k < lFilters.size(); k++)
                if (lFacilities.get(j).toLowerCase().equals(lFilters.get(k).toLowerCase()))
                    return true;

        return false;
    }

    public ArrayList<CRoom> getRooms() {
        ArrayList<CRoom> roomsHotelBefore = hotel.getArrayRooms();
        ArrayList<CRoom> roomsHotelAfter = new ArrayList<>();

        for (int i=0; i<roomsHotelBefore.size(); i++)
        {
            ArrayList<String> aFacilities = roomsHotelBefore.get(i).getArrayFacilitiesRoom();

            if (checkRoomHasFacility(aFacilities, listFilters))
                roomsHotelAfter.add(roomsHotelBefore.get(i));
        }

        return roomsHotelAfter;
    }
}
