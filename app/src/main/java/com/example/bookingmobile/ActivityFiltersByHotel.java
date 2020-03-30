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
                // sharedPreferences to send the dates and numbers
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ActivityFiltersByHotel.this);
                SharedPreferences.Editor editor = sharedPref.edit();

                if (sharedPref.contains("DOUBLE_BED")) {
                    editor.putBoolean("DOUBLE_BED", chboxDoubleBed.isSelected());
                    editor.apply();
                }
                else {
                    editor.putBoolean("DOUBLE_BED", chboxDoubleBed.isSelected());
                    editor.commit();
                }

                if (sharedPref.contains("FREE_WIFI")) {
                    editor.putBoolean("FREE_WIFI", chboxFreeWifi.isSelected());
                    editor.apply();
                }
                else {
                    editor.putBoolean("FREE_WIFI", chboxFreeWifi.isSelected());
                    editor.commit();
                }

                if (sharedPref.contains("GYM")) {
                    editor.putBoolean("GYM", chboxGym.isSelected());
                    editor.apply();
                }
                else {
                    editor.putBoolean("GYM", chboxGym.isSelected());
                    editor.commit();
                }

                if (sharedPref.contains("PARKING")) {
                    editor.putBoolean("PARKING", chboxParking.isSelected());
                    editor.apply();
                }
                else {
                    editor.putBoolean("PARKING", chboxParking.isSelected());
                    editor.commit();
                }

                if (sharedPref.contains("PET_FRIENDLY")) {
                    editor.putBoolean("PET_FRIENDLY", chboxPetFriendly.isSelected());
                    editor.apply();
                }
                else {
                    editor.putBoolean("PET_FRIENDLY", chboxPetFriendly.isSelected());
                    editor.commit();
                }

                if (sharedPref.contains("SAUNA")) {
                    editor.putBoolean("SAUNA", chboxSauna.isSelected());
                    editor.apply();
                }
                else {
                    editor.putBoolean("SAUNA", chboxSauna.isSelected());
                    editor.commit();
                }

                if (sharedPref.contains("SMOKING_FREE")) {
                    editor.putBoolean("SMOKING_FREE", chboxSmokingFree.isSelected());
                    editor.apply();
                }
                else {
                    editor.putBoolean("SMOKING_FREE", chboxSmokingFree.isSelected());
                    editor.commit();
                }

                if (sharedPref.contains("SWIMMING_POOL")) {
                    editor.putBoolean("SWIMMING_POOL", chboxSwimmingPool.isSelected());
                    editor.apply();
                }
                else {
                    editor.putBoolean("SWIMMING_POOL", chboxSwimmingPool.isSelected());
                    editor.commit();
                }

                Intent intent = new Intent( ActivityFiltersByHotel.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
