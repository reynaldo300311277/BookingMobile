package com.example.bookingmobile;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CUser implements Parcelable
{
    private int pkUser;
    private String loginName;
    private String hashPassword;
    private String email;

    private ArrayList<CPaymentInfo> paymentInfos = new ArrayList<>();
    private ArrayList<CBooking> bookings = new ArrayList<>();

    public CUser (int pkUser, String loginName, String hashPassword, String email) {
        this.pkUser = pkUser;
        this.loginName = loginName;
        this.hashPassword = hashPassword;
        this.email = email;
    }

    private CUser (int pkUser, String loginName, String hashPassword, String email,
                   ArrayList<CPaymentInfo> paymentInfos, ArrayList<CBooking> bookings) {
        this.pkUser = pkUser;
        this.loginName = loginName;
        this.hashPassword = hashPassword;
        this.email = email;
        this.paymentInfos = paymentInfos;
        this.bookings = bookings;
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

    public static CUser addUser(SQLiteDatabase db, String loginName,
                                String hashPassword, String email){
        SQLiteDatabase mDatabase = db;

        // check if user exist
        String queryUser = "SELECT loginName " +
                "FROM User " +
                "WHERE loginName = '" + loginName + "'";

        Cursor cursorUser;
        cursorUser = mDatabase.rawQuery(queryUser,null);

        if (cursorUser.getCount() > 0) {
            return null;
        }

        // insert the booking on database
        ContentValues val = new ContentValues();
        val.put("loginName", loginName);
        val.put("hashPassword", hashPassword);
        val.put("email", email);

        try {
            long result = db.insert("User",null, val);

            if (result == -1)
                return null;
        }
        catch (Exception ex){
            return null;
        }

        String queryInsert = "INSERT INTO User(loginName, hashPassword, email) " +
                "VALUES ('" + loginName + "', '" + hashPassword + "', '" + email + "');";

        Cursor cursorInsertion = mDatabase.rawQuery(queryInsert, null);
        cursorInsertion.close();

        // retrieve the new user to return
        String queryNewUser = "SELECT pkUser, loginName, hashPassword, email " +
                "FROM User " +
                "WHERE loginName = '" + loginName + "';";

        Cursor cursorNewUser;
        cursorNewUser = mDatabase.rawQuery(queryNewUser,null);
        cursorNewUser.moveToFirst();
        int pkUser = cursorNewUser.getInt(cursorNewUser.getColumnIndex("pkUser"));

        return new CUser(pkUser, loginName, hashPassword, email);
    }

    public static boolean checkAuthentication(SQLiteDatabase db,  String loginName,
                                       String hashPassword) {
        SQLiteDatabase mDatabase = db;

        String queryUser = "SELECT pkUser, loginName, hashPassword, email " +
                "FROM User " +
                "WHERE loginName LIKE '" + loginName + "' " +
                "AND hashPassword LIKE '" + hashPassword + "'";

        Cursor cursorUser = mDatabase.rawQuery(queryUser, null);

        if (cursorUser.getCount() == 0) { return false; }

        return true;
    }

    public static CUser getAllDataFromUser(SQLiteDatabase db,  String loginName,
                                       String hashPassword) {
        SQLiteDatabase mDatabase = db;

        String queryUser = "SELECT pkUser, loginName, hashPassword, email " +
                "FROM User " +
                "WHERE loginName LIKE '" + loginName + "' " +
                "AND hashPassword LIKE '" + hashPassword + "'";

        Cursor cursorUser = mDatabase.rawQuery(queryUser, null);

        if (cursorUser.getCount() == 0) {
            return null;
        }

        cursorUser.moveToFirst();

        int pkUser = cursorUser.getInt(cursorUser.getColumnIndex("pkUser"));
        String email = cursorUser.getString(cursorUser.getColumnIndex("email"));

        // select all user paymento
        Cursor cursorPayment;
        ArrayList<CPaymentInfo> paymentInfos = new ArrayList<>();

        String queryPayment = "SELECT pkPayment, credCardName, credCardType, credCardNumber, " +
                "credCardExpire, credCardCVC, status " +
                "FROM PaymentInfo " +
                "WHERE fkUser = '" + pkUser + "'";

        cursorPayment = mDatabase.rawQuery(queryPayment,null);

        if (cursorPayment.moveToFirst()) {

            for (int i = 0; i < cursorPayment.getCount(); i++) {

                String credCardName = cursorPayment.getString(cursorPayment.getColumnIndex("credCardName"));
                String credCardType = cursorPayment.getString(cursorPayment.getColumnIndex("credCardType"));
                String credCardNumber = cursorPayment.getString(cursorPayment.getColumnIndex("credCardNumber"));
                String credCardExpire = cursorPayment.getString(cursorPayment.getColumnIndex("credCardExpire"));
                String credCardCVC = cursorPayment.getString(cursorPayment.getColumnIndex("credCardCVC"));
                String status = cursorPayment.getString(cursorPayment.getColumnIndex("status"));

                CPaymentInfo paymentInfo = new CPaymentInfo(pkUser, credCardName, credCardType,
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
                "WHERE fkUser = '" + pkUser + "'";

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

        return new CUser(pkUser, loginName, hashPassword, email, paymentInfos, bookings);
    }
}
