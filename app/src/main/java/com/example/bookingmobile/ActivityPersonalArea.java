package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityPersonalArea extends AppCompatActivity
        implements MyRecyclerViewAdapterBookings.ItemClickListener{

    MyRecyclerViewAdapterBookings adapterBookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_area);

        // define a SharedPreferences to be used
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        int user_id = sharedPref.getInt("USER_ID", -1);

        final CSQLiteHelper dbHelper = new CSQLiteHelper((getApplicationContext()));
        dbHelper.createDatabaseConnection();
        SQLiteDatabase mDatabase = dbHelper.getReadableDatabase();

        try
        {
            Cursor cursor;

            String query = "SELECT b.dateCheckIn, b.dateCheckOut, u.loginName, u.email, r.type, " +
                        "h.name, h.city " +
                    "FROM Booking AS b " +
                        "INNER JOIN User AS u ON b.fkUser = u.pkUser " +
                        "INNER JOIN Booking_Room AS br ON b.pkBooking = br.fkBooking " +
                        "INNER JOIN Room AS r ON br.fkRoom = r.pkRoom " +
                        "INNER JOIN Hotel AS h ON r.fkHotel = h.pkHotel " +
                        "WHERE u.pkUser = " + user_id + ";";

            cursor = mDatabase.rawQuery(query,null);
            int total = cursor.getCount();

            if (cursor.getCount() != 0) {
                cursor.moveToFirst();

                String userLogin = cursor.getString(cursor.getColumnIndex("loginName"));
                String userEmail = cursor.getString(cursor.getColumnIndex("email"));

                // store the set of bookings
                ArrayList<String[]> listBookings = new ArrayList<>();

                do
                {
                    String hotelName = cursor.getString(cursor.getColumnIndex("name"));
                    String hoteCity = cursor.getString(cursor.getColumnIndex("city"));
                    String roomType = cursor.getString(cursor.getColumnIndex("type"));
                    String dateCheckIn = cursor.getString(cursor.getColumnIndex("dateCheckIn"));
                    String dateCheckOut = cursor.getString(cursor.getColumnIndex("dateCheckOut"));

                    String[] sBookings = new String[] {userLogin, userEmail, hotelName, hoteCity,
                            roomType, dateCheckIn, dateCheckOut};

                    listBookings.add(sBookings);
                }
                while (cursor.moveToNext());

                cursor.close();

                TextView txtUserLogin = findViewById(R.id.txtUserLogin);
                TextView txtUserEmail = findViewById(R.id.txtUserEmail);

                txtUserLogin.setText(userLogin);
                txtUserEmail.setText(userEmail);

                // set up the RecyclerView
                final RecyclerView recyclerView = findViewById(R.id.recyclerViewBookings);
                recyclerView.setLayoutManager(new LinearLayoutManager(ActivityPersonalArea.this));
                adapterBookings = new MyRecyclerViewAdapterBookings(ActivityPersonalArea.this, listBookings);
                adapterBookings.setClickListener(ActivityPersonalArea.this);
                recyclerView.setAdapter(adapterBookings);
            }
        }
        catch (Exception e)
        {
        }

        Button btnPersonalNewSearch = findViewById(R.id.btnPersonalNewSearch);
        btnPersonalNewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityPersonalArea.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
    }
}
