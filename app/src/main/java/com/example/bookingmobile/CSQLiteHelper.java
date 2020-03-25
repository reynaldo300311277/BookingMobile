package com.example.bookingmobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CSQLiteHelper extends SQLiteOpenHelper
{
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

    public void createDatabaseConnection()  {
        if (!checkDatabase())
        {
            this.getReadableDatabase();

            try
            {
                copyDatabase();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
