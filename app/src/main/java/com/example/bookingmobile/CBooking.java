package com.example.bookingmobile;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CBooking implements Parcelable
{
    private SQLiteDatabase mDatabase;

    private int pkBooking;
    private int fkRoom;
    private int fkUser;
    private String dateCheckIn;
    private String dateCheckOut;
    private int numAdults;
    private int numChildren;
    private String status;
    private double totalFeePerNight;
    private String credCardName;
    private String credCardType;
    private String credCardNumber;
    private String credCardExpire;
    private String credCardCVC;

    public CBooking(SQLiteDatabase mDatabase) {
        this.mDatabase = mDatabase;
    }

    public CBooking(int pkBooking, int fkUser, String dateCheckIn, String dateCheckOut,
                    int numAdults, int numChildren, String status, double totalFeePerNight,
                    String credCardName, String credCardType, String credCardNumber,
                    String credCardExpire, String credCardCVC) {
        this.pkBooking = pkBooking;
        this.fkUser = fkUser;
        this.dateCheckIn = dateCheckIn;
        this.dateCheckOut = dateCheckOut;
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

    protected CBooking(Parcel in) {
        pkBooking = in.readInt();
        fkRoom = in.readInt();
        fkUser = in.readInt();
        dateCheckIn = in.readString();
        dateCheckOut = in.readString();
        numAdults = in.readInt();
        numChildren = in.readInt();
        status = in.readString();
        totalFeePerNight = in.readDouble();
        credCardName = in.readString();
        credCardType = in.readString();
        credCardNumber = in.readString();
        credCardExpire = in.readString();
        credCardCVC = in.readString();
    }

    public static final Creator<CBooking> CREATOR = new Creator<CBooking>() {
        @Override
        public CBooking createFromParcel(Parcel in) {
            return new CBooking(in);
        }

        @Override
        public CBooking[] newArray(int size) {
            return new CBooking[size];
        }
    };

    public int getpkBooking() {
        return pkBooking;
    }

    public void setpkBooking(int pkBooking) {
        this.pkBooking = pkBooking;
    }

    public int getFkRoom() {
        return fkRoom;
    }

    public void setFkRoom(int fkRoom) {
        this.fkRoom = fkRoom;
    }

    public int getFkUser() {
        return fkUser;
    }

    public void setFkUser(int fkUser) {
        this.fkUser = fkUser;
    }

    public String getdateCheckIn() {
        return dateCheckIn;
    }

    public void setdateCheckIn(String dateCheckIn) {
        this.dateCheckIn = dateCheckIn;
    }

    public String getdateCheckOut() {
        return dateCheckOut;
    }

    public void setdateCheckOut(String dateCheckOut) {
        this.dateCheckOut = dateCheckOut;
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

    public double getTotalFeePerNight() {
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

    public boolean addBooking (int fkRoom, int fkUser, String dateCheckIn, String dateCheckOut,
                               int numAdults, int numChildren, String status, double totalFeePerNight,
                               String credCardName, String credCardType, String credCardNumber,
                               String credCardExpire, String credCardCVC) {
        try {
            // check if these values were filled
            if (dateCheckIn.isEmpty() || dateCheckOut.isEmpty() || status.isEmpty() ||
                    credCardName.isEmpty() || credCardType.isEmpty() || credCardNumber.isEmpty() ||
                    credCardExpire.isEmpty() || credCardCVC.isEmpty())
                return false;

            // check if these values are positives and less than the threshold
            if (fkUser< 0 || numAdults < 0 || numChildren < 0 || totalFeePerNight < 0 ||
                    fkUser > 1100 || numAdults > 4 || numChildren > 4)
                return false;

            // check if the format of check-in/check-out/expire date is correct
            if (!isDateValid(dateCheckIn, "yyyy-MM-dd") &&
                    !isDateValid(dateCheckOut, "yyyy-MM-dd") &&
                    !isDateValid(credCardExpire, "yyyy-MM"))
                return false;

            // check if check-in date is before the check-out
            if (dateCheckIn.compareTo(dateCheckOut) >= 0)
                return false;

            // check if the credit card and cvc are numerics
            if (!isNumeric(credCardNumber) || !isNumeric(credCardCVC))
                return false;

            // check if the type of credit card is VISA or MASTERCARD
            if (!(credCardType.toLowerCase().equals("visa") || credCardType.toLowerCase().equals("mastercard")))
                return false;

            // check if the card name is not empty
            if (credCardName.isEmpty())
                return false;

            // check if fkUser is integer and exist
            Cursor cursorUser;

            // check if user exist
            String queryUser = "SELECT pkUser " +
                    "FROM User " +
                    "WHERE pkUser = '" + fkUser + "'";

            cursorUser = mDatabase.rawQuery(queryUser,null);

            // user does not exist - returns and it is necessary to enter the user before
            if (cursorUser.getCount() == 0) {
                mDatabase.close();
                return false;
            }

            // check if fkUser is integer and exist
            Cursor cursorRoom;

            // check if room exist
            String queryRoom = "SELECT pkRoom " +
                    "FROM Room " +
                    "WHERE pkRoom = '" + fkRoom + "'";

            cursorRoom = mDatabase.rawQuery(queryRoom,null);

            // room does not exist
            if (cursorRoom.getCount() == 0) {
                mDatabase.close();
                return false;
            }

            this.fkRoom = fkRoom;

            // insert the booking no database
            String queryInsertBook = "INSERT INTO Booking(fkUser, dateCheckIn, dateCheckOut, " +
                        "numAdults, numChildren, status, totalFeePerNight, credCardName, " +
                        "credCardType, credCardNumber, credCardExpire, credCardCVC) " +
                    "VALUES (" + fkUser + ", '" + dateCheckIn + "', '" + dateCheckOut + "', " +
                        numAdults + ", " + numChildren + ", '" + status + "', " +
                        totalFeePerNight + ", '" +  credCardName + "', '" + credCardType + "', '" +
                        credCardNumber + "', '" + credCardExpire + "', '" + credCardCVC + "');";

            Cursor cursorInsertion = mDatabase.rawQuery(queryInsertBook, null);
            cursorInsertion.moveToLast();
            cursorInsertion.close();

            // get the last inserted pkBooking
            Cursor cursorLastBooking;

            String queryLastBooking = "SELECT seq FROM sqlite_sequence WHERE name LIKE 'Booking'";
            cursorLastBooking = mDatabase.rawQuery(queryLastBooking, null);
            cursorLastBooking.moveToFirst();
            int lastPkBooking = cursorLastBooking.getInt(cursorLastBooking.getColumnIndex("seq"));

            // insert the relationship between the booking and the room
            String queryRelation = "INSERT INTO Booking_Room(fkBooking, fkRoom) VALUES (" +
                    lastPkBooking + ", " + fkRoom + ");";

            Cursor cursorRelation = mDatabase.rawQuery(queryRelation, null);
            cursorRelation.moveToFirst();
            cursorRelation.close();
        }
        // if any conversion failed throws the exception and return false
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        mDatabase.close();
        return true;
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        }
        catch (NumberFormatException ex){
            return false;
        }
    }

    private boolean isDateValid(String dateToValidate, String dateFromat){

        if(dateToValidate == null){
            return false;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try {
            //if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pkBooking);
        dest.writeInt(fkRoom);
        dest.writeInt(fkUser);
        dest.writeString(dateCheckIn);
        dest.writeString(dateCheckOut);
        dest.writeInt(numAdults);
        dest.writeInt(numChildren);
        dest.writeString(status);
        dest.writeDouble(totalFeePerNight);
        dest.writeString(credCardName);
        dest.writeString(credCardType);
        dest.writeString(credCardNumber);
        dest.writeString(credCardExpire);
        dest.writeString(credCardCVC);
    }
}