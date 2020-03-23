package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class testParceable extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_parceable);

        Intent intent = getIntent();
        CHotel cHotel = intent.getParcelableExtra("CHOTEL");

        ArrayList<CRoom> cRooms = cHotel.getArrayRooms();

        TextView txtTest = findViewById(R.id.txtView1);
        txtTest.setText(cRooms.get(0).getPrice() + "");
    }
}
