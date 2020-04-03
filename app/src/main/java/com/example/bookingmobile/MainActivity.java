package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
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
    private NumberPicker numberPickerRoom;
    private NumberPicker numberPickerAdults;
    private NumberPicker numberPickerChildren;

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

        SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMM");

        textViewDateIn.setText(monthDateFormat.format(calIn.getTime()) + "/" +
                calIn.get(Calendar.DAY_OF_MONTH) + "/" +  calIn.get(Calendar.YEAR));
        textViewDateOut.setText(monthDateFormat.format(calOut.getTime()) + "/" +
                calOut.get(Calendar.DAY_OF_MONTH) + "/" +  calOut.get(Calendar.YEAR));

        numberPickerRoom = findViewById(R.id.numberPickerRooms);
        numberPickerAdults = findViewById(R.id.numberPickerAdults);
        numberPickerChildren = findViewById(R.id.numberPickerChildren);

        numberPickerRoom.setMinValue(1);
        numberPickerRoom.setMaxValue(4);
        numberPickerRoom.setWrapSelectorWheel(false);

        numberPickerAdults.setMinValue(0);
        numberPickerAdults.setMaxValue(4);
        numberPickerAdults.setWrapSelectorWheel(false);

        numberPickerChildren.setMinValue(0);
        numberPickerChildren.setMaxValue(4);
        numberPickerChildren.setWrapSelectorWheel(false);

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
            }
        });

        textViewDateOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Toast.makeText(MainActivity.this,"Booking cannot be in the past",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                calIn.set(year,month,dayOfMonth);
//                SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMM");
//                String strDate = monthDateFormat.format(calIn.getTime()) + "/" + dayOfMonth + "/" + year;

                SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMM/dd/yyyy");
                String strDate = monthDateFormat.format(calIn.getTime());

                textViewDateIn.setText(strDate);

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
                    Toast.makeText(MainActivity.this,
                            "Booking end date must be after start date", Toast.LENGTH_LONG).show();
                    return;
                }

                calOut.set(year,month,dayOfMonth);
                SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMM");
                String strDate = monthDateFormat.format(calOut.getTime()) + "/" + dayOfMonth + "/" + year;
                textViewDateOut.setText(strDate);
            }
        };

        // define a SharedPreferences to be used
        sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        editor = sharedPref.edit();

        // initialize the SharedPreferences with default value;

        // tem que colocar a verificação se existe pois senão vai dar pau => quando volta do final da reserva pode dar pau...
        // ******************************************************************************************************************
        editor.putInt("USER_ID", 1);                            // mudar este valor para -1 quando tivermos a autenticação pronta
        editor.putString("USER_NAME", "rhardson0");             // mudar este valor para "" quando tivermos autenticação pronta
        editor.putString("HASH_PASSWORD", "73872041");          // mudar este valor para "" quando tivermos autenticação pronta
        editor.putInt("ROOM_SELECTED", -1);
        editor.putString("HOTEL_NAME", "");
        editor.putString("HOTEL_CITY", "");
        editor.putString("ROOM_TYPE", "");
        editor.putString("DATE_IN", "");
        editor.putString("DATE_OUT", "");
        editor.putInt("TOTAL_DAYS", -1);
        editor.putFloat("TOTAL_PRICE", 0);
        editor.putInt("NUM_ROOMS", 1);
        editor.putInt("NUM_ADULTS", 2);
        editor.putInt("NUM_CHILDREN", 1);
        editor.putString("CARD_NAME", "");
        editor.putString("CARD_TYPE", "");
        editor.putString("CARD_NUMBER", "");
        editor.putString("CARD_EXPIRE_DATE", "");
        editor.putString("CARD_CVC", "");
        editor.commit();

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String city = spinnerDestinyCities.getSelectedItem().toString();
                int numRooms = numberPickerRoom.getValue();
                int numAdults = numberPickerAdults.getValue();
                int numChildren = numberPickerChildren.getValue();
                SimpleDateFormat ymdDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateIn = ymdDateFormat.format(calIn.getTime());
                String dateOut = ymdDateFormat.format(calOut.getTime());

                if ((numAdults + numChildren) > 0)
                {
                    try {
                        // update the values in SharedPreferences
                        editor.putString("DATE_IN", dateIn);
                        editor.putString("DATE_OUT", dateOut);
                        editor.putInt("NUM_ROOMS", numRooms);
                        editor.putInt("NUM_ADULTS", numAdults);
                        editor.putInt("NUM_CHILDREN", numChildren);
                        editor.apply();

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
                else
                {
                    Toast.makeText(MainActivity.this,
                            "No Guest Information", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btnHotelFilter = findViewById(R.id.btnHotelFilter);
        btnHotelFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
