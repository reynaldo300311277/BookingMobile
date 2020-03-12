package com.example.bookingmobile;

public class CSightseeing
{
    private int pkSightseeing;
    private int fkHotel;
    private String name;
    private float distance;

    public CSightseeing(int pkSightseeing, int fkHotel, String name, float distance) {
        this.pkSightseeing = pkSightseeing;
        this.fkHotel = fkHotel;
        this.name = name;
        this.distance = distance;
    }

    public int getPkSightseeing() {
        return pkSightseeing;
    }

    public void setPkSightseeing(int pkSightseeing) {
        this.pkSightseeing = pkSightseeing;
    }

    public int getFkHotel() {
        return fkHotel;
    }

    public void setFkHotel(int fkHotel) {
        this.fkHotel = fkHotel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
