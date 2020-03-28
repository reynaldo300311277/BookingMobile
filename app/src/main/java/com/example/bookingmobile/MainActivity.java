package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
{
    CSQLiteHelper dbHelper;
//    Button btnGetData;

    private TextView textViewDateIn;
    private TextView textViewDateOut;
    private DatePickerDialog.OnDateSetListener datePickerDialogListenerIn;
    private DatePickerDialog.OnDateSetListener datePickerDialogListenerOut;
    private Calendar calIn;
    private Calendar calOut;

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

        textViewDateIn.setText(calIn.get(Calendar.MONTH)+1 + "/" + calIn.get(Calendar.DAY_OF_MONTH) + "/" +
                        calIn.get(Calendar.YEAR));
        textViewDateOut.setText(calOut.get(Calendar.MONTH)+1 + "/" + calOut.get(Calendar.DAY_OF_MONTH) + "/" +
                calOut.get(Calendar.YEAR));

        Spinner spinnerDestinyCities = (Spinner) findViewById(R.id.spinnerDestinyCities);
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
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
