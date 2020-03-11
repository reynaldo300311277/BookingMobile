package com.example.bookingmobile;

public class CPhotos {

    private int pkPhoto;
    private int fkRoom;
    private int idxImage;

    public CPhotos(int pkPhoto, int fkRoom, int idxImage) {
        this.pkPhoto = pkPhoto;
        this.fkRoom = fkRoom;
        this.idxImage = idxImage;
    }

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
}
