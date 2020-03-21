package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    CSQLiteHelper dbHelper;
    Button btnGetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the object and database connection
        dbHelper = new CSQLiteHelper((getApplicationContext()));
        dbHelper.createDatabaseConnection();

        btnGetData = findViewById(R.id.btnGetData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an CHotelsFromCity and set the filters
                CHotelsFromCity hotelsFromCity = new CHotelsFromCity(dbHelper.getReadableDatabase());
                String test = hotelsFromCity.setFiltersHotels(true,true,
                        true,true, true, true,
                        true,true);

                // Get the hotels
                ArrayList<CHotel> arrayListHotels = hotelsFromCity.getHotelsFromCity("Edmonton",
                        "2019-06-07","2019-06-10",1,4,
                        4);

                // Create an object with all rooms from the selected hotel
                CRoomsFromHotel roomsFromHotel = new CRoomsFromHotel(arrayListHotels.get(0));

                // Set the filters for rooms
                roomsFromHotel.setFiltersRooms(false, false, true,
                        false, false, false);

                // Get the set of filtered rooms
                ArrayList<CRoom> filteredRooms = roomsFromHotel.getRooms();

                CUser user = new CUser("pbattersrr", "48925241");
                // return false if user does not exist   -   'pbattersrr'    '48925241'
                 if (user.checkAuthentication(dbHelper.getReadableDatabase()))
                    Toast.makeText(MainActivity.this,"Output => " +
                        user.getPaymentInfos().size(), Toast.LENGTH_SHORT).show();
                 else
                     Toast.makeText(MainActivity.this,"Output => User DOES NOT Exist",
                             Toast.LENGTH_SHORT).show();

                 // creating and inserting a new booking - it is open to insertion
                 CBooking newBooking = new CBooking(dbHelper.getWritableDatabase());

                 if (newBooking.addBooking(1,1,"2020-02-01",
                         "2020-02-05",2,1,"1",
                         150.0,"John Paul Felipe III",
                         "MasterCard","1234567890123456",
                         "2022-01","987"))
                     Toast.makeText(MainActivity.this,"Booking inserted ",
                             Toast.LENGTH_SHORT).show();
                 else
                     Toast.makeText(MainActivity.this,"Booking NOT inserted",
                             Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
