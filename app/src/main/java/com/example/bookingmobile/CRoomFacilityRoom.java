package com.example.bookingmobile;

public class CRoomFacilityRoom {

    private int fkRoom;
    private int fkRoomFacility;

    public CRoomFacilityRoom(int fkRoom, int fkRoomFacility) {
        this.fkRoom = fkRoom;
        this.fkRoomFacility = fkRoomFacility;
    }

    public int getFkRoom() {
        return fkRoom;
    }

    public void setFkRoom(int fkRoom) {
        this.fkRoom = fkRoom;
    }

    public int getFkRoomFacility() {
        return fkRoomFacility;
    }

    public void setFkRoomFacility(int fkRoomFacility) {
        this.fkRoomFacility = fkRoomFacility;
    }
}
