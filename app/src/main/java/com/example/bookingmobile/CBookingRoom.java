package com.example.bookingmobile;

public class CBookingRoom {

    private int fkBooking;
    private int fkRoom;

    public CBookingRoom(int fkBooking, int fkRoom) {
        this.fkBooking = fkBooking;
        this.fkRoom = fkRoom;
    }

    private String type;

    public int getFkBooking() {
        return fkBooking;
    }

    public void setFkBooking(int fkBooking) {
        this.fkBooking = fkBooking;
    }

    public int getFkRoom() {
        return fkRoom;
    }

    public void setFkRoom(int fkRoom) {
        this.fkRoom = fkRoom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
