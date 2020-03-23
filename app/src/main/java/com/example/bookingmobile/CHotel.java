package com.example.bookingmobile;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CHotel implements Parcelable
{
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

    protected CHotel(Parcel in) {
        pkHotel = in.readInt();
        name = in.readString();
        city = in.readString();
        province = in.readString();
        description = in.readString();
        latitude = in.readFloat();
        longitude = in.readFloat();
        arrayFacilitiesHotel = in.createStringArrayList();
        arrayRooms = in.createTypedArrayList(CRoom.CREATOR);
        arraySightseeing = in.createTypedArrayList(CSightseeing.CREATOR);
    }

    public static final Creator<CHotel> CREATOR = new Creator<CHotel>() {
        @Override
        public CHotel createFromParcel(Parcel in) {
            return new CHotel(in);
        }

        @Override
        public CHotel[] newArray(int size) {
            return new CHotel[size];
        }
    };

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

    public ArrayList<CRoom> getArrayRooms() {
        return arrayRooms;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pkHotel);
        dest.writeString(name);
        dest.writeString(city);
        dest.writeString(province);
        dest.writeString(description);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        dest.writeStringList(arrayFacilitiesHotel);
        dest.writeTypedList(arrayRooms);
        dest.writeTypedList(arraySightseeing);
    }
}
