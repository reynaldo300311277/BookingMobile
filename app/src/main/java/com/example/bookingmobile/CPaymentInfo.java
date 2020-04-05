package com.example.bookingmobile;

import android.os.Parcel;
import android.os.Parcelable;

public class CPaymentInfo implements Parcelable
{
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

    protected CPaymentInfo(Parcel in) {
        fkUser = in.readInt();
        credCardName = in.readString();
        credCardType = in.readString();
        credCardNumber = in.readString();
        credCardExpire = in.readString();
        credCardCVC = in.readString();
        status = in.readString();
    }

    public static final Creator<CPaymentInfo> CREATOR = new Creator<CPaymentInfo>() {
        @Override
        public CPaymentInfo createFromParcel(Parcel in) {
            return new CPaymentInfo(in);
        }

        @Override
        public CPaymentInfo[] newArray(int size) {
            return new CPaymentInfo[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(fkUser);
        dest.writeString(credCardName);
        dest.writeString(credCardType);
        dest.writeString(credCardNumber);
        dest.writeString(credCardExpire);
        dest.writeString(credCardCVC);
        dest.writeString(status);
    }
}
