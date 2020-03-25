package com.example.bookingmobile;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CRoom implements Parcelable
{
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

    private ArrayList<String> arrayFacilitiesRoom = new ArrayList<>();
    private ArrayList<CPhotos> arrayPhotos = new ArrayList<>();

    public CRoom(int pkRoom, int fkHotel)  {
        this.pkRoom = pkRoom;
        this.fkHotel = fkHotel;
        this.price = -1;
        this.type = "";
        this.description = "";
        this.maxNumAdults = 4;
        this.maxNumchildren = 4;
        this.status = "";
        this.statusBegin = null;
        this.statusEnd = null;
    }

    protected CRoom(Parcel in) {
        pkRoom = in.readInt();
        fkHotel = in.readInt();
        price = in.readFloat();
        type = in.readString();
        description = in.readString();
        maxNumAdults = in.readInt();
        maxNumchildren = in.readInt();
        status = in.readString();
        statusBegin = in.readString();
        statusEnd = in.readString();
        arrayFacilitiesRoom = in.createStringArrayList();
    }

    public static final Creator<CRoom> CREATOR = new Creator<CRoom>() {
        @Override
        public CRoom createFromParcel(Parcel in) {
            return new CRoom(in);
        }

        @Override
        public CRoom[] newArray(int size) {
            return new CRoom[size];
        }
    };

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

    public ArrayList<String> getFacilitiesRoom() {
        return arrayFacilitiesRoom;
    }

    public void setFacilitiesRoom(ArrayList<String> arrayFacilitiesRoom) {
        this.arrayFacilitiesRoom = arrayFacilitiesRoom;
    }

    public ArrayList<CPhotos> getSightseeing() {
        return arrayPhotos;
    }

    public void setPhotos(ArrayList<CPhotos> arrayPhotos) {
        this.arrayPhotos = arrayPhotos;
    }

    public ArrayList<String> getArrayFacilitiesRoom() {
        return arrayFacilitiesRoom;
    }

    public ArrayList<CPhotos> getArrayPhotos() {
        return arrayPhotos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pkRoom);
        dest.writeInt(fkHotel);
        dest.writeFloat(price);
        dest.writeString(type);
        dest.writeString(description);
        dest.writeInt(maxNumAdults);
        dest.writeInt(maxNumchildren);
        dest.writeString(status);
        dest.writeString(statusBegin);
        dest.writeString(statusEnd);
        dest.writeStringList(arrayFacilitiesRoom);
    }
}
