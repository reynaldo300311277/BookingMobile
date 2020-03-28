package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ActivityRoomFilter extends AppCompatActivity implements MyRecyclerViewAdapterRooms.ItemClickListener{

    MyRecyclerViewAdapterRooms adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_filter);

        // data to populate the RecyclerView with
        ArrayList<String[]> animalNames = new ArrayList<>();

        String[] val1 = new String[] {"Horse", "$ 200.00"};
        String[] val2 = new String[] {"Cow", "$ 300.00"};
        String[] val3 = new String[] {"Camel", "$ 400.00"};
        String[] val4 = new String[] {"Sheep", "$ 500.00"};
        String[] val5 = new String[] {"Goat", "$ 600.00"};
        String[] val6 = new String[] {"Horse", "$ 700.00"};
        String[] val7 = new String[] {"Horse", "$ 200.00"};
        String[] val8 = new String[] {"Cow", "$ 300.00"};
        String[] val9 = new String[] {"Camel", "$ 400.00"};
        animalNames.add(val1);
        animalNames.add(val2);
        animalNames.add(val3);
        animalNames.add(val4);
        animalNames.add(val5);
        animalNames.add(val6);
        animalNames.add(val7);
        animalNames.add(val8);
        animalNames.add(val9);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewRooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapterRooms(this, animalNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(View view, int position) {
    }
}
