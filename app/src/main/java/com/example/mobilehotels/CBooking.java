package com.example.bookingmobile;

public class CBooking {

    private int fkUser;
    private String dataCheckIn;
    private String dataCheckOut;
    private int numAdults;
    private int numChildren;
    private String status;
    private float totalFeePerNight;
    private String credCardName;
    private String credCardType;
    private String credCardNumber;
    private String credCardExpire;
    private String credCardCVC;

    public CBooking(int fkUser, String dataCheckIn, String dataCheckOut, int numAdults,
                    int numChildren, String status, float totalFeePerNight, String credCardName,
                    String credCardType, String credCardNumber, String credCardExpire,
                    String credCardCVC) {
        this.fkUser = fkUser;
        this.dataCheckIn = dataCheckIn;
        this.dataCheckOut = dataCheckOut;
        this.numAdults = numAdults;
        this.numChildren = numChildren;
        this.status = status;
        this.totalFeePerNight = totalFeePerNight;
        this.credCardName = credCardName;
        this.credCardType = credCardType;
        this.credCardNumber = credCardNumber;
        this.credCardExpire = credCardExpire;
        this.credCardCVC = credCardCVC;
    }

    public int getFkUser() {
        return fkUser;
    }

    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
    }

    public String getDataCheckIn() {
        return dataCheckIn;
    }

    public void setDataCheckIn(String dataCheckIn) {
        this.dataCheckIn = dataCheckIn;
    }

    public String getDataCheckOut() {
        return dataCheckOut;
    }

    public void setDataCheckOut(String dataCheckOut) {
        this.dataCheckOut = dataCheckOut;
    }

    public int getNumAdults() {
        return numAdults;
    }

    public void setNumAdults(int numAdults) {
        this.numAdults = numAdults;
    }

    public int getNumChildren() {
        return numChildren;
    }

    public void setNumChildren(int numChildren) {
        this.numChildren = numChildren;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getTotalFeePerNight() {
        return totalFeePerNight;
    }

    public void setTotalFeePerNight(float totalFeePerNight) {
        this.totalFeePerNight = totalFeePerNight;
    }

    public String getCredCardName() {
        return credCardName;
    }

    public void setCredCardName(String credCardName) {
        this.credCardName = credCardName;
    }

    public String getCredCardType() {
        return credCardType;
    }

    public void setCredCardType(String credCardType) {
        this.credCardType = credCardType;
    }

    public String getCredCardNumber() {
        return credCardNumber;
    }

    public void setCredCardNumber(String credCardNumber) {
        this.credCardNumber = credCardNumber;
    }

    public String getCredCardExpire() {
        return credCardExpire;
    }

    public void setCredCardExpire(String credCardExpire) {
        this.credCardExpire = credCardExpire;
    }

    public String getCredCardCVC() {
        return credCardCVC;
    }

    public void setCredCardCVC(String credCardCVC) {
        this.credCardCVC = credCardCVC;
    }
}