package com.example.bookingmobile;

import android.os.Parcel;
import android.os.Parcelable;

public class CPhotos implements Parcelable
{
    private int pkPhoto;
    private int fkRoom;
    private int idxImage;

    public CPhotos(int pkPhoto, int fkRoom, int idxImage) {
        this.pkPhoto = pkPhoto;
        this.fkRoom = fkRoom;
        this.idxImage = idxImage;
    }

    protected CPhotos(Parcel in) {
        pkPhoto = in.readInt();
        fkRoom = in.readInt();
        idxImage = in.readInt();
    }

    public static final Creator<CPhotos> CREATOR = new Creator<CPhotos>() {
        @Override
        public CPhotos createFromParcel(Parcel in) {
            return new CPhotos(in);
        }

        @Override
        public CPhotos[] newArray(int size) {
            return new CPhotos[size];
        }
    };

    public int getPkPhoto() {
        return pkPhoto;
    }

    public void setPkPhoto(int pkPhoto) {
        this.pkPhoto = pkPhoto;
    }

    public int getFkRoom() {
        return fkRoom;
    }

    public void setFkRoom(int fkRoom) {
        this.fkRoom = fkRoom;
    }

    public int getIdxImage() {
        return idxImage;
    }

    public void setIdxImage(int IdxImage) {
        this.idxImage = IdxImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pkPhoto);
        dest.writeInt(fkRoom);
        dest.writeInt(idxImage);
    }
}
