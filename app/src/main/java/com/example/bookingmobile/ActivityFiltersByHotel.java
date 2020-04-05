package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityFiltersByHotel extends AppCompatActivity {

    CheckBox chboxDoubleBed;
    CheckBox chboxFreeWifi;
    CheckBox chboxGym;
    CheckBox chboxParking;
    CheckBox chboxPetFriendly;
    CheckBox chboxSauna;
    CheckBox chboxSmokingFree;
    CheckBox chboxSwimmingPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters_by_hotel);

        chboxDoubleBed = findViewById(R.id.chboxDoubleBed);
        chboxFreeWifi = findViewById(R.id.chboxFreeWifi);
        chboxGym = findViewById(R.id.chboxGym);
        chboxParking = findViewById(R.id.chboxParking);
        chboxPetFriendly = findViewById(R.id.chboxPetFriendly);
        chboxSauna = findViewById(R.id.chboxSauna);
        chboxSmokingFree = findViewById(R.id.chboxSmokingFree);
        chboxSwimmingPool = findViewById(R.id.chboxSwimmingPool);

        Button btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}
