package com.example.bookingmobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CUser implements Parcelable
{
    private SQLiteDatabase mDatabase;

    private int pkUser;
    private String loginName;
    private String hashPassword;
    private String email;

    private ArrayList<CPaymentInfo> paymentInfos = new ArrayList<>();
    private ArrayList<CBooking> bookings = new ArrayList<>();

    public CUser (String loginName, String hashPassword) {
        this.pkUser = -1;
        this.loginName = loginName;
        this.hashPassword = hashPassword;
        this.email = "";
    }

    public CUser (int pkUser, String loginName, String hashPassword, String email) {
        this.pkUser = pkUser;
        this.loginName = loginName;
        this.hashPassword = hashPassword;
        this.email = email;
    }

    protected CUser(Parcel in) {
        pkUser = in.readInt();
        loginName = in.readString();
        hashPassword = in.readString();
        email = in.readString();
    }

    public static final Creator<CUser> CREATOR = new Creator<CUser>() {
        @Override
        public CUser createFromParcel(Parcel in) {
            return new CUser(in);
        }

        @Override
        public CUser[] newArray(int size) {
            return new CUser[size];
        }
    };

    public int getPkUser() {
        return pkUser;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<CPaymentInfo> getPaymentInfos() {
        return paymentInfos;
    }

    public void setPaymentInfos(ArrayList<CPaymentInfo> paymentInfos) {
        this.paymentInfos = paymentInfos;
    }

    public ArrayList<CBooking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<CBooking> bookings) {
        this.bookings = bookings;
    }

    public boolean checkAuthentication(SQLiteDatabase mDatabase) {

        this.mDatabase = mDatabase;
        Cursor cursorUser;

        // check if user exist
        String queryUser = "SELECT pkUser, loginName, hashPassword, email " +
                "FROM User " +
                "WHERE loginName LIKE '" + this.loginName + "' " +
                "AND hashPassword LIKE '" + this.hashPassword + "'";

        cursorUser = mDatabase.rawQuery(queryUser,null);

        if (cursorUser.getCount() == 0) {
            mDatabase.close();
            return false;
        }

        cursorUser.moveToFirst();

        this.pkUser = cursorUser.getInt(cursorUser.getColumnIndex("pkUser"));
        this.email = cursorUser.getString(cursorUser.getColumnIndex("email"));

        // select all user paymento
        Cursor cursorPayment;
        ArrayList<CPaymentInfo> paymentInfos = new ArrayList<>();

        String queryPayment = "SELECT pkPayment, credCardName, credCardType, credCardNumber, " +
                "credCardExpire, credCardCVC, status " +
                "FROM PaymentInfo " +
                "WHERE fkUser = '" + this.pkUser + "'";

        cursorPayment = mDatabase.rawQuery(queryPayment,null);

        if (cursorPayment.moveToFirst()) {

            for (int i = 0; i < cursorPayment.getCount(); i++) {

                String credCardName = cursorPayment.getString(cursorPayment.getColumnIndex("credCardName"));
                String credCardType = cursorPayment.getString(cursorPayment.getColumnIndex("credCardType"));
                String credCardNumber = cursorPayment.getString(cursorPayment.getColumnIndex("credCardNumber"));
                String credCardExpire = cursorPayment.getString(cursorPayment.getColumnIndex("credCardExpire"));
                String credCardCVC = cursorPayment.getString(cursorPayment.getColumnIndex("credCardCVC"));
                String status = cursorPayment.getString(cursorPayment.getColumnIndex("status"));

                CPaymentInfo paymentInfo = new CPaymentInfo(this.pkUser, credCardName, credCardType,
                        credCardNumber, credCardExpire, credCardCVC, status);

                paymentInfos.add(paymentInfo);
            }
        }

        // select all user's bookings (historic)
        Cursor cursorBooking;
        ArrayList<CBooking> bookings = new ArrayList<>();

        String queryBooking = "SELECT pkBooking, fkUser, dateCheckIn, dateCheckOut, numAdults, " +
                "numChildren, status, totalFeePerNight, credCardName, credCardType, " +
                "credCardNumber, credCardExpire, credCardCVC " +
                "FROM Booking " +
                "WHERE fkUser = '" + this.pkUser + "'";

        cursorBooking = mDatabase.rawQuery(queryBooking,null);

        if (cursorBooking.moveToFirst()) {

            for (int i = 0; i < cursorBooking.getCount(); i++) {

                int pkBooking = cursorBooking.getInt(cursorBooking.getColumnIndex("pkBooking"));
                int fkUser = cursorBooking.getInt(cursorBooking.getColumnIndex("fkUser"));
                String dateCheckIn = cursorBooking.getString(cursorBooking.getColumnIndex("dateCheckIn"));
                String dateCheckOut = cursorBooking.getString(cursorBooking.getColumnIndex("dateCheckOut"));
                int numAdults = cursorBooking.getInt(cursorBooking.getColumnIndex("numAdults"));
                int numChildren = cursorBooking.getInt(cursorBooking.getColumnIndex("numChildren"));
                String status = cursorPayment.getString(cursorPayment.getColumnIndex("status"));
                float totalFeePerNight = cursorBooking.getFloat(cursorBooking.getColumnIndex("totalFeePerNight"));
                String credCardName = cursorBooking.getString(cursorBooking.getColumnIndex("credCardName"));
                String credCardType = cursorBooking.getString(cursorBooking.getColumnIndex("credCardType"));
                String credCardNumber = cursorBooking.getString(cursorBooking.getColumnIndex("credCardNumber"));
                String credCardExpire = cursorBooking.getString(cursorBooking.getColumnIndex("credCardExpire"));
                String credCardCVC = cursorBooking.getString(cursorBooking.getColumnIndex("credCardCVC"));

                CBooking booking = new CBooking(pkBooking, fkUser, dateCheckIn, dateCheckOut,
                        numAdults, numChildren, status, totalFeePerNight, credCardName,
                        credCardType, credCardNumber, credCardExpire, credCardCVC);

                bookings.add(booking);
            }
        }

        mDatabase.close();
        this.paymentInfos = paymentInfos;
        this.bookings = bookings;

        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pkUser);
        dest.writeString(loginName);
        dest.writeString(hashPassword);
        dest.writeString(email);
    }
}
