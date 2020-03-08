package com.example.bookingmobile;

public class CHotelHotelFacility {

    private int fkHotel;
    private int fkHotelFacility;

    public CHotelHotelFacility(int fkHotel, int fkHotelFacility) {
        this.fkHotel = fkHotel;
        this.fkHotelFacility = fkHotelFacility;
    }

    public int getFkHotel() {
        return fkHotel;
    }

    public void setFkHotel(int fkHotel) {
        this.fkHotel = fkHotel;
    }

    public int getFkHotelFacility() {
        return fkHotelFacility;
    }

    public void setFkHotelFacility(int fkHotelFacility) {
        this.fkHotelFacility = fkHotelFacility;
    }
}
