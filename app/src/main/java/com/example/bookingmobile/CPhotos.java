package com.example.bookingmobile;

public class CPhotos {

    private int fkRoom;
    private String path;

    public CPhotos(int fkRoom, String path) {
        this.fkRoom = fkRoom;
        this.path = path;
    }

    public int getFkRoom() {
        return fkRoom;
    }

    public void setFkRoom(int fkRoom) {
        this.fkRoom = fkRoom;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
