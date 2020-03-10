package com.example.bookingmobile;

public class CRoom {

    private int pkRoom;
    private int fkHotel;
    private float price;
    private String type;
    private String description;
    private int maxNumAdults;
    private int maxNumchildren;
    private String status;
    private String statusBegin;
    private String statusEnd;

    public CRoom(int pkRoom)
    {
        this.pkRoom = pkRoom;
    }

    public CRoom(int pkRoom, int fkHotel, float price, String type, String description,
                 int maxNumAdults, int maxNumchildren, String status)
    {
        this.pkRoom = pkRoom;
        this.fkHotel = fkHotel;
        this.price = price;
        this.type = type;
        this.description = description;
        this.maxNumAdults = maxNumAdults;
        this.maxNumchildren = maxNumchildren;
        this.status = status;
        this.statusBegin = null;
        this.statusEnd = null;
    }

    public int getPkRoom() {
        return pkRoom;
    }

    public void setPkRoom(int pkRoom) {
        this.pkRoom = pkRoom;
    }

    public int getFkHotel() {
        return fkHotel;
    }

    public void setFkHotel(int fkHotel) {
        this.fkHotel = fkHotel;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMaxNumAdults() {
        return maxNumAdults;
    }

    public void setMaxNumAdults(int maxNumAdults) {
        this.maxNumAdults = maxNumAdults;
    }

    public int getMaxNumchildren() {
        return maxNumchildren;
    }

    public void setMaxNumchildren(int maxNumchildren) {
        this.maxNumchildren = maxNumchildren;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusBegin() {
        return statusBegin;
    }

    public void setStatusBegin(String statusBegin) {
        this.statusBegin = statusBegin;
    }

    public String getStatusEnd() {
        return statusEnd;
    }

    public void setStatusEnd(String statusEnd) {
        this.statusEnd = statusEnd;
    }
}
