package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ActivityConfirmationView extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_view);

        // define a SharedPreferences to be used
        sharedPref = PreferenceManager.getDefaultSharedPreferences(ActivityConfirmationView.this);
        editor = sharedPref.edit();

        TextView txtConfirmViewHotel = findViewById(R.id.txtConfirmViewHotel);
        TextView txtConfirmViewCity = findViewById(R.id.txtConfirmViewCity);
        TextView txtConfirmViewDateIn = findViewById(R.id.txtConfirmViewDateIn);
        TextView txtConfirmViewDateOut = findViewById(R.id.txtConfirmViewDateOut);
        TextView txtConfirmViewNumbers = findViewById(R.id.txtConfirmViewNumbers);
        TextView txtConfirmViewTotalPrice = findViewById(R.id.txtConfirmViewPrice);
        TextView txtConfirmViewRoomType = findViewById(R.id.txtConfirmViewRoomType);
        TextView txtConfirmViewPayment = findViewById(R.id.txtConfirmViewPayment);

        txtConfirmViewHotel.setText(sharedPref.getString("HOTEL_NAME", ""));
        txtConfirmViewCity.setText(sharedPref.getString("HOTEL_CITY", ""));
        txtConfirmViewRoomType.setText(sharedPref.getString("ROOM_TYPE", ""));
        txtConfirmViewDateIn.setText(sharedPref.getString("DATE_IN", ""));
        txtConfirmViewDateOut.setText(sharedPref.getString("DATE_OUT", ""));
        txtConfirmViewNumbers.setText("#Rooms: " + sharedPref.getInt("NUM_ROOMS", -1) +
                "\n#Adults: " + sharedPref.getInt("NUM_ADULTS", -1) +
                "\n#Children: " + sharedPref.getInt("NUM_CHILDREN", -1));
        String card_number = sharedPref.getString("CARD_NUMBER", "");
        txtConfirmViewPayment.setText(sharedPref.getString("CARD_TYPE", "") +
                " - ends  *" + card_number.substring(card_number.length() - 4));
        txtConfirmViewTotalPrice.setText("$" + String.format("%.2f",
                sharedPref.getFloat("TOTAL_PRICE", -1)));

        Button btnNewSearch = findViewById(R.id.btnNewSearch);
        btnNewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityConfirmationView.this,
                        MainActivity.class);
                startActivity(intent);
            }
        });

        Button btnPersonalArea = findViewById(R.id.btnPersonalArea);
        btnPersonalArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityConfirmationView.this,
                        ActivityPersonalArea.class);
                startActivity(intent);
            }
        });

    }
}
