package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ActivityRoomFilter extends AppCompatActivity implements MyRecyclerViewAdapterRooms.ItemClickListener{

    MyRecyclerViewAdapterRooms adapter;

    private String dateIn;
    private String dateOut;
    private String numRooms;
    private String numAdults;
    private String numChildren;
    private String city;
    private String hotelName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_filter);

        Intent intent = getIntent();
        CHotel cHotel = intent.getParcelableExtra("CHOTEL");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        this.dateIn = sharedPref.getString("DATE_IN", "01-01-2020");
        this.dateOut = sharedPref.getString("DATE_OUT", "01-02-2020");
        this.numRooms = String.valueOf(sharedPref.getInt("NUM_ROOMS", 1));
        this.numAdults = String.valueOf(sharedPref.getInt("NUM_ADULTS", 1));
        this.numChildren = String.valueOf(sharedPref.getInt("NUM_CHILDREN", 0));
        this.city = cHotel.getCity();
        this.hotelName = cHotel.getName();

        TextView txtDateIn = findViewById(R.id.txtCheckIn);
        TextView txtDateOut = findViewById(R.id.txtCheckOut);
        TextView txtNumRooms = findViewById(R.id.txtNumRooms);
        TextView txtNumAdults = findViewById(R.id.txtNumAdults);
        TextView txtNumChildren = findViewById(R.id.txtNumChildren);
        TextView txtCity = findViewById(R.id.txtCity);
        TextView txtHotelName = findViewById(R.id.txtHotel);

        txtDateIn.setText(this.dateIn);
        txtDateOut.setText(this.dateOut);
        txtNumRooms.setText(this.numRooms);
        txtNumAdults.setText(this.numAdults);
        txtNumChildren.setText(this.numChildren);
        txtCity.setText(this.city);
        txtHotelName.setText(this.hotelName);

 /*       ArrayList<CRoom> cRooms = cHotel.getArrayRooms();

        ArrayList<String[]> rooms  = new ArrayList<>();

        for (int i=0; i<cRooms.size(); i++)
        {
            String roomType = cRooms.get(i).getType();
            String price = String.valueOf(cRooms.get(i).getPrice());

            rooms.add(new String[]{roomType, price});
        }
*/

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapterRooms(this, cHotel);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
    }
}
