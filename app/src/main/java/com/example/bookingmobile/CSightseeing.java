package com.example.bookingmobile;

import android.os.Parcel;
import android.os.Parcelable;

public class CSightseeing implements Parcelable
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

    protected CSightseeing(Parcel in) {
        pkSightseeing = in.readInt();
        fkHotel = in.readInt();
        name = in.readString();
        distance = in.readFloat();
    }

    public static final Creator<CSightseeing> CREATOR = new Creator<CSightseeing>() {
        @Override
        public CSightseeing createFromParcel(Parcel in) {
            return new CSightseeing(in);
        }

        @Override
        public CSightseeing[] newArray(int size) {
            return new CSightseeing[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pkSightseeing);
        dest.writeInt(fkHotel);
        dest.writeString(name);
        dest.writeFloat(distance);
    }
}
