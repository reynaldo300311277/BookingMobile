package com.example.bookingmobile;

import java.util.ArrayList;

public class CHotel {

    private int pkHotel;
    private String name;
    private String city;
    private String province;
    private String description;
    private float latitude;
    private float longitude;

    private ArrayList<String> arrayFacilitiesHotel = new ArrayList<>();
    private ArrayList<CRoom> arrayRooms = new ArrayList<>();
    private ArrayList<CSightseeing> arraySightseeing = new ArrayList<>();

    public CHotel(int pkHotel) {
        this.pkHotel = pkHotel;
        this.name = "";
        this.city = "";
        this.province = "";
        this.description = "";
        this.latitude = -1;
        this.longitude = -1;
    }

    public CHotel(int pkHotel, String name, String city, String province, String description,
                  float latitude, float longitude) {
        this.pkHotel = pkHotel;
        this.name = name;
        this.city = city;
        this.province = province;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getPkHotel() {
        return pkHotel;
    }

    public void setPkHotel(int pkHotel) {
        this.pkHotel = pkHotel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void addRoom(CRoom room) { this.arrayRooms.add(room);  }

    public CHotel getHotel(int pkHotel) {
        if (this.pkHotel == pkHotel)
            return this;
        else
            return null;
    }

    public ArrayList<String> getFacilitiesHotel() {
        return arrayFacilitiesHotel;
    }

    public void setFacilitiesHotel(ArrayList<String> arrayFacilitiesHotel) {
        this.arrayFacilitiesHotel = arrayFacilitiesHotel;
    }

    public ArrayList<CSightseeing> getSightseeing() {
        return arraySightseeing;
    }

    public void setSightseeing(ArrayList<CSightseeing> arrayFacilitiesHotel) {
        this.arraySightseeing = arrayFacilitiesHotel;
    }
}
