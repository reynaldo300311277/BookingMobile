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

public class MainActivity extends AppCompatActivity {

    List<CUser> lstUser = new ArrayList<CUser>();
    Button btnGetData;
    Button btnCheckUser;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetData = findViewById(R.id.btnGetData);
        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CSQLiteHelper dbHelper = new CSQLiteHelper((getApplicationContext()));
                dbHelper.createDatabaseConnection();

                // Get all hotels from a specific city
                CHotelsFromCity hotelsFromCity = new CHotelsFromCity(dbHelper.getWritableDatabase());
                String test = hotelsFromCity.setFiltersHotels(true,true,
                        true,true, true, true,
                        true,true);

                // array with a set of Hotels from Edmonton from "2019-06-07" to "2019-06-10" with
                // at top 4 adults and 4 children.
                ArrayList<CHotel> arrayListHotels = hotelsFromCity.getHotelsFromCity("Edmonton",
                        "2019-06-07","2019-06-10",1,4,
                        4);

                CRoomsFromHotel cRoomsFromHotel = new CRoomsFromHotel(
                        dbHelper.getWritableDatabase(), arrayListHotels.get(0).getPkHotel());

                Toast.makeText(MainActivity.this,
                        "Total Hotels => " + arrayListHotels.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
