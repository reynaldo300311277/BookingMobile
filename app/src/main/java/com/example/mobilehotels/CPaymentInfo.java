package com.example.bookingmobile;

public class CPaymentInfo {

    private int fkUser;
    private String credCardName;
    private String credCardType;
    private String credCardNumber;
    private String credCardExpire;
    private String credCardCVC;
    private String status;

    public CPaymentInfo(int fkUser, String credCardName, String credCardType,
                        String credCardNumber, String credCardExpire,
                        String credCardCVC, String status) {
        this.fkUser = fkUser;
        this.credCardName = credCardName;
        this.credCardType = credCardType;
        this.credCardNumber = credCardNumber;
        this.credCardExpire = credCardExpire;
        this.credCardCVC = credCardCVC;
        this.status = status;
    }

    public int getFkUser() {
        return fkUser;
    }

    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
