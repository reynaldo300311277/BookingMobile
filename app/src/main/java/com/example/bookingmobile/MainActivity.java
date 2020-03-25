package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

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

                // INTERFACE #1
                // Create a CHotelsFromCity and set the filters
                CHotelsFromCity hotelsFromCity = new CHotelsFromCity(dbHelper.getReadableDatabase());
                String test = hotelsFromCity.setFiltersHotels(true,true,
                        true,true, true, true,
                        true,true);

                // Get the hotels
                ArrayList<CHotel> arrayListHotels = hotelsFromCity.getHotelsFromCity("Edmonton",
                        "2019-06-07","2019-06-10",1,4,
                        4);


                // HOW TO PASS A SELECT HOTEL FROM INTERFACE #1 TO INTERFACE #2
                // ***** PARCEABLE *****
                // How to pass an Object using Parceable => it is the same like a String or Integer
                //Intent intent = new Intent(MainActivity.this, testParceable.class);
                //intent.putExtra("CHOTEL", arrayListHotels.get(0));
                //startActivity(intent);


                // INTERFACE #2
                // Create a CRoomsFromHotel from the selected hotel and set the filters for rooms
                CRoomsFromHotel roomsFromHotel = new CRoomsFromHotel(arrayListHotels.get(0));
                roomsFromHotel.setFiltersRooms(false, false, true,
                        false, false, false);

                // Get the rooms
                ArrayList<CRoom> filteredRooms = roomsFromHotel.getRooms();


                // CHECK AUTHENTICATION OF USER - INTERFACE #f
                // return false if user does not exist
                 if (CUser.checkAuthentication(dbHelper.getReadableDatabase(),
                         "qmillar31", "39698562"))
                 {
                     // Retrieve all information related to the user
                     CUser user = CUser.getAllDataFromUser(dbHelper.getReadableDatabase(),
                             "qmillar31","39698562");

                     Toast.makeText(MainActivity.this, "Output => " +
                             user.getPaymentInfos().size(), Toast.LENGTH_SHORT).show();


                     // DELETING A BOOKING - INTERFACE #5 - OPÇÃO ÍCONE 'x'
                     // when it needs to delete an booking, it must be used "GetWritableDatabase"
                     CBookingsPerUser bookingsPerUserBefore = new CBookingsPerUser(dbHelper.getWritableDatabase(), user);
                     bookingsPerUserBefore.deleteBooking(383);
                     // ***** code just to check - not necessary in the application
                     //CBookingsPerUser bookingsPerUserAfter = new CBookingsPerUser(dbHelper.getReadableDatabase(), user);
                     //int sizeBeforeDeletion = bookingsPerUserBefore.getBookingsPerUser().size();
                     ///int sizeAfterDeletion = bookingsPerUserAfter.getBookingsPerUser().size();
                 }
                 else
                     Toast.makeText(MainActivity.this,"Output => User DOES NOT Exist",
                             Toast.LENGTH_SHORT).show();


                 // INCLUDE A NEW USER - INTERFACE #f
                 CUser newUser = CUser.addUser(dbHelper.getWritableDatabase(),
                         "pbattersrrx","48925241", "testemail");

                 if (newUser == null)
                     Toast.makeText(MainActivity.this,"User already exist",
                             Toast.LENGTH_SHORT).show();
                 else {
                     int key = newUser.getPkUser();
                     Toast.makeText(MainActivity.this, "User created successfully => key " + key,
                             Toast.LENGTH_SHORT).show();
                 }


                 // INTERFACE #3
                // creating and inserting a new booking - it is open to insertion
                 CBooking newBooking = CBooking.addBooking(dbHelper.getWritableDatabase(),
                         1,1,"2020-03-03","2020-03-08",
                         3,0,"1",255.0,
                         "John Paul Felipe III","MasterCard",
                         "1234567890123456","2025-12",
                         "987");

                 if (newBooking != null)
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
