package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapterHotels.ItemClickListener
{
    CSQLiteHelper dbHelper;

    private TextView textViewDateIn;
    private TextView textViewDateOut;
    private DatePickerDialog.OnDateSetListener datePickerDialogListenerIn;
    private DatePickerDialog.OnDateSetListener datePickerDialogListenerOut;
    private Calendar calIn;
    private Calendar calOut;
    private Spinner spinnerDestinyCities;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    MyRecyclerViewAdapterHotels adapterHotels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the object and database connection
        dbHelper = new CSQLiteHelper((getApplicationContext()));
        dbHelper.createDatabaseConnection();

        textViewDateIn = (TextView) findViewById(R.id.editTextDateIn);
        textViewDateOut = (TextView) findViewById(R.id.editTextDateOut);

        calIn = Calendar.getInstance();
        calOut = Calendar.getInstance();
        calOut.add(Calendar.DAY_OF_YEAR, 7);

        textViewDateIn.setText(calIn.get(Calendar.MONTH)+1 + "/" +
                calIn.get(Calendar.DAY_OF_MONTH) + "/" +  calIn.get(Calendar.YEAR));
        textViewDateOut.setText(calOut.get(Calendar.MONTH)+1 + "/" +
                calOut.get(Calendar.DAY_OF_MONTH) + "/" +  calOut.get(Calendar.YEAR));

        // retrieve all cities
        ArrayList<String> cities = CDestinations.getDestinations(dbHelper.getReadableDatabase());

        spinnerDestinyCities = (Spinner) findViewById(R.id.spinnerDestinyCities);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.textDestinyCities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDestinyCities.setAdapter(adapter);

        textViewDateIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start editing

                int year = calIn.get(Calendar.YEAR);
                int month = calIn.get(Calendar.MONTH);
                int day = calIn.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialogIn = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        datePickerDialogListenerIn,
                        year, month, day
                        );

                datePickerDialogIn.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialogIn.show();

                // Finish editing
            }
        });

        textViewDateOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start editing

                int year = calOut.get(Calendar.YEAR);
                int month = calOut.get(Calendar.MONTH);
                int day = calOut.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialogOut = new DatePickerDialog(MainActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog,
                        datePickerDialogListenerOut,
                        year, month, day
                );

                datePickerDialogOut.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialogOut.show();

                // Finish editing
            }
        });

        datePickerDialogListenerIn = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calToday = Calendar.getInstance();
                calToday.set(Calendar.HOUR_OF_DAY, 0);
                calToday.set(Calendar.MINUTE, 0);
                calToday.set(Calendar.SECOND, 0);
                calToday.set(Calendar.MILLISECOND, 0);

                Calendar calTest = Calendar.getInstance();
                calTest.set(year,month,dayOfMonth);

                if ( calToday.compareTo(calTest) == 1 ) {
                    Toast.makeText(MainActivity.this,"Booking cannot be in the past", Toast.LENGTH_LONG).show();
                    return;
                }

                String strDate = month+1 + "/" + dayOfMonth + "/" + year;
                textViewDateIn.setText(strDate);
                calIn.set(year,month,dayOfMonth);
            }
        };

        datePickerDialogListenerOut = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar calTest = Calendar.getInstance();
                calTest.set(year,month,dayOfMonth);
                calTest.set(Calendar.HOUR_OF_DAY, 0);
                calTest.set(Calendar.MINUTE, 0);
                calTest.set(Calendar.SECOND, 0);
                calTest.set(Calendar.MILLISECOND, 0);

                if ( calIn.compareTo(calTest) != -1 ) {
                    Toast.makeText(MainActivity.this,"Booking end date must be after start date", Toast.LENGTH_LONG).show();
                    return;
                }

                String strDate = month+1 + "/" + dayOfMonth + "/" + year;
                textViewDateOut.setText(strDate);
                calOut.set(year,month,dayOfMonth);
            }
        };

        // sharedPreferences to send the dates and numbers
        sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        //SharedPreferences sharedPreferences=this.getSharedPreferences("com.example.bookingmobile", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    NumberPicker numberPickerRooms = findViewById(R.id.numberPickerRooms);
                    NumberPicker numberPickerAdults = findViewById(R.id.numberPickerAdults);
                    NumberPicker numberPickerChildren = findViewById(R.id.numberPickerChildren);

                    // Reynaldo => review...
                    // ***********************************************************************************************
                    // RIGHT VALUES - BUT NOT USED UNTIL THE OBJECTS ARE FIXED !!!!!!!
                    // ***********************************************************************************************
                    String city = spinnerDestinyCities.getSelectedItem().toString();
                    String dateIn = textViewDateIn.getText().toString();    // MUST HAVE THE FORMAT "MM-DD-YYYY"
                    String dateOut = textViewDateOut.getText().toString();  // MUST HAVE THE FORMAT "MM-DD-YYYY"
                    int numRooms = numberPickerRooms.getValue();            // COMPONENT IS NOT WORKING CORRECTLY!!!
                    int numAdults = numberPickerAdults.getValue();          // COMPONENT IS NOT WORKING CORRECTLY!!!
                    int numChildren = numberPickerChildren.getValue();      // COMPONENT IS NOT WORKING CORRECTLY!!!

                    // fake values to run => this code must be deleted when the above lines are ok!!!
                    dateIn = "2019-06-07";
                    dateOut = "2019-06-10";
                    numRooms = 1;
                    numAdults = 2;
                    numChildren = 1;
                    city = "Edmonton";

                    // save all general information on shared preferences
                    // TALVEZ É LIXO
                    if (sharedPref.contains("DATE_IN")) {
                        editor.putString("DATE_IN", dateIn);
                        editor.apply();
                    }
                    else {
                        editor.putString("DATE_IN", dateIn);
                        editor.commit();
                    }

                    if (sharedPref.contains("DATE_OUT")) {
                        editor.putString("DATE_OUT", dateOut);
                        editor.apply();
                    }
                    else {
                        editor.putString("DATE_OUT", dateOut);
                        editor.commit();
                    }

                    if (sharedPref.contains("NUM_ROOMS")) {
                        editor.putInt("NUM_ROOMS", numRooms);
                        editor.apply();
                    }
                    else {
                        editor.putInt("NUM_ROOMS", numRooms);
                        editor.commit();
                    }


                    if (sharedPref.contains("NUM_ADULTS")) {
                        editor.putInt("NUM_ADULTS", numAdults);
                        editor.apply();
                    }
                    else {
                        editor.putInt("NUM_ADULTS", numAdults);
                        editor.commit();
                    }


                    if (sharedPref.contains("NUM_CHILDREN")) {
                        editor.putInt("NUM_CHILDREN", numChildren);
                        editor.apply();
                    }
                    else {
                        editor.putInt("NUM_CHILDREN", numChildren);
                        editor.commit();
                    }
                    // ATÉ AQUI!!

                    boolean hasParking = true;
                    boolean hasDoubleBed = true;
                    boolean hasFreeWifi = true;
                    boolean isSmokingFree = true;
                    boolean isPetFriendly = true;
                    boolean hasGym = true;
                    boolean hasSwimmingPool = true;
                    boolean hasSauna = true;

                    // Create a CHotelsFromCity and set the filters
                    CHotelsFromCity hotelsFromCity = new CHotelsFromCity(dbHelper.getReadableDatabase());
                    String test = hotelsFromCity.setFiltersHotels(hasParking,hasDoubleBed, hasFreeWifi,
                            isSmokingFree, isPetFriendly, hasGym, hasSwimmingPool, hasSauna);

                    // Get the hotels
                    ArrayList<CHotel> arrayListHotels = hotelsFromCity.getHotelsFromCity(city,
                            dateIn, dateOut, numRooms,numAdults, numChildren);

                    ArrayList<String[]> hotelInfoDisplay = new ArrayList<>();

                    for (int i=0; i<arrayListHotels.size(); i++)
                    {
                        CHotel hotel = arrayListHotels.get(i);
                        String nameHotel = hotel.getName();

                        ArrayList<CRoom> rooms = hotel.getArrayRooms();
                        float minPriceHotel = 100000;

                        for (int j=0; j< rooms.size(); j++)
                            if (minPriceHotel > rooms.get(j).getPrice())
                                minPriceHotel = rooms.get(j).getPrice();

                        String[] val = new String[] {nameHotel, Float.toString(minPriceHotel)};
                        hotelInfoDisplay.add(val);
                    }

                    // set up the RecyclerView
                    RecyclerView recyclerView = findViewById(R.id.recyclerViewHotels);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    // tenho que passar o vetor com os hoteis e também o próprio objeto
                    adapterHotels = new MyRecyclerViewAdapterHotels(MainActivity.this,
                            hotelInfoDisplay, arrayListHotels);
                    adapterHotels.setClickListener(MainActivity.this);
                    recyclerView.setAdapter(adapterHotels);
                }
                catch (Exception ex) {
                }
            }
        });

        Button btnHotelFilter = findViewById(R.id.btnHotelFilter);
        btnHotelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent = new Intent(MainActivity.this, ActivityFiltersByHotel.class);
                //startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }

    @Override
    public void onItemClick(View view, int position) {
    }
}
