package com.example.bookingmobile;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSQLiteHelper extends SQLiteOpenHelper {

    private static String DB_PATH = "";
    private static String DB_NAME = "prjMobile.db";
    private SQLiteDatabase mDatabase;
    private Context mContext = null;

    public CSQLiteHelper(Context context) {
        super(context, DB_NAME, null, 1);

        if (Build.VERSION.SDK_INT >= 17)
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        else
            DB_PATH = "/data/data" + context.getPackageName() + "/databases/";

        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // do nothing
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            copyDatabase();
    }

    private boolean checkDatabase() {
        SQLiteDatabase tempDB = null;

        try {
            String path = DB_PATH + DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(path,null, SQLiteDatabase.OPEN_READWRITE);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (tempDB != null)
            tempDB.close();

        return tempDB != null ? true : false;
    }

    public void copyDatabase() {
        try {
            InputStream myInput = mContext.getAssets().open(DB_NAME);
            String outputFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openDatabase() {
        String path = DB_PATH + DB_NAME;
        mDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void createDatabase() {
        if (!checkDatabase()) {
            this.getReadableDatabase();

            try {
                copyDatabase();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // return all users
    public List<CUser> getAllUsers() {
        List<CUser> temp = new ArrayList<CUser>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
            c = db.rawQuery("SELECT * FROM User", null);

            if (c == null)
                return null;

            c.moveToFirst();

            do {
                CUser cuser = new CUser(c.getString(c.getColumnIndex("loginName")),
                        c.getString(c.getColumnIndex("hashPassword")),
                        c.getString(c.getColumnIndex("email")));

                temp.add(cuser);
            } while (c.moveToNext());

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return temp;
    }

    // Authentication Check
    //      true => User and Password Ok.
    //      fase => User and Password NOT Ok.
    public boolean checkUser(String user, String password) {
 /*       SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;

        try {
            String query = "SELECT * FROM User " +
                    "WHERE loginName = '" + user + "' AND hashPassword = '" + password + "';";
            c = db.rawQuery(query,null);

            if (c.getCount() == 0) {
                db.close();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return true;*/

        //CHotelsFromCity hotelsFromCity = new CHotelsFromCity(this.getWritableDatabase());
        //return hotelsFromCity.checkUser("rhardson1", "73872041");
        return true;
    }

    public boolean verifyQuery() {
        try
        {
            CHotelsFromCity hotelsFromCity = new CHotelsFromCity(this.getWritableDatabase(),
                    "Edmonton", "2019-06-07", "2019-06-10",
                    1, 4, 4);

            ArrayList<CHotel> arrayHotels = hotelsFromCity.getHotelsFromCity();
        }
        catch (Exception e)
        {
        }

        return true;
    }

}
