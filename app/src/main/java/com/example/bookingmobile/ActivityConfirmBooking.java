package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class ActivityConfirmBooking extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private CHotel hotel;
    private CRoom room;

    private EditText edtxtCardName;
    private EditText edtxtCardNumber;
    private NumberPicker numberPickerYearCC;
    private NumberPicker numberPickerMonthCC;
    private EditText edtxtCardCVC;

    TextView txtConfirmHotel;
    TextView txtConfirmCity;
    TextView txtConfirmRoomType;
    TextView txtConfirmNumbers;
    TextView txtConfirmDateIn;
    TextView txtConfirmDateOut;
    TextView txtConfirmPrice;

    ImageView imageViewCC;

    private Button btnConfirmBooking;

    private String dateIn;
    private String dateOut;
    private int numRooms;
    private int numAdults;
    private int numChildren;
    private int roomSelected;
    private int totalDays;

    float pricePerNight;

    private String[] monthVals = new String[] {"JAN","FEB","MAR","APR","MAI","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        // define a SharedPreferences to be used
        sharedPref = PreferenceManager.getDefaultSharedPreferences(ActivityConfirmBooking.this);
        editor = sharedPref.edit();

        final CSQLiteHelper dbHelper = new CSQLiteHelper((getApplicationContext()));
        dbHelper.createDatabaseConnection();

        dateIn = sharedPref.getString("DATE_IN", "2019-06-07");
        dateOut = sharedPref.getString("DATE_OUT", "2019-06-10");
        numRooms = sharedPref.getInt("NUM_ROOMS", 1);
        numAdults = sharedPref.getInt("NUM_ADULTS", 2);
        numChildren = sharedPref.getInt("NUM_CHILDREN", 1);
        roomSelected = sharedPref.getInt("ROOM_SELECTED", 0);

        Log.w("BOOK", roomSelected + "");

        Intent intent = getIntent();
        this.hotel = intent.getParcelableExtra("CHOTEL");

        room = hotel.getArrayRooms().get(roomSelected);

        txtConfirmHotel = findViewById(R.id.txtConfirmHotel);
        txtConfirmCity = findViewById(R.id.txtConfirmCity);
        txtConfirmRoomType = findViewById(R.id.txtConfirmRoomType);
        txtConfirmNumbers = findViewById(R.id.txtConfirmNumbers);
        txtConfirmDateIn = findViewById(R.id.txtConfirmDateIn);
        txtConfirmDateOut = findViewById(R.id.txtConfirmDateOut);
        txtConfirmPrice = findViewById(R.id.txtConfirmPrice);

        edtxtCardName = findViewById(R.id.edtxtCardName);
        edtxtCardNumber = findViewById(R.id.edtxtCardNumber);
        numberPickerMonthCC = findViewById(R.id.numberPickerMonthCC);
        numberPickerYearCC = findViewById(R.id.numberPickerYearCC);
        edtxtCardCVC = findViewById(R.id.edtxtCardCVC);

        imageViewCC = findViewById(R.id.imageViewCC);

        numberPickerMonthCC.setDisplayedValues(monthVals);
        numberPickerMonthCC.setMinValue(0);
        numberPickerMonthCC.setMaxValue(monthVals.length-1);
        numberPickerMonthCC.setWrapSelectorWheel(false);

        numberPickerYearCC.setMinValue(2020);
        numberPickerYearCC.setMaxValue(2025);
        numberPickerYearCC.setWrapSelectorWheel(false);

        btnConfirmBooking = findViewById(R.id.btnConfirmBooking);

        SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dIn = sdfYmd.parse(dateIn);
            Date dOut = sdfYmd.parse(dateOut);
            long diff = dOut.getTime() - dIn.getTime();
            totalDays = Math.round(diff / (24 * 60 * 60 * 1000));
        } catch (ParseException ex) {
            Toast.makeText(ActivityConfirmBooking.this,"A Critical Error Happened", Toast.LENGTH_LONG).show();
            return;
        }

        txtConfirmHotel.setText(hotel.getName());
        txtConfirmCity.setText(hotel.getCity());
        txtConfirmRoomType.setText(room.getType());
        txtConfirmNumbers.setText("#Rooms: " + numRooms + "\n#Adults: " + numAdults + "\n#Children: " + numChildren);
        txtConfirmDateIn.setText(dateIn);
        txtConfirmDateOut.setText(dateOut);

        pricePerNight = room.getPrice();
        final float finalPrice = pricePerNight * totalDays;

        Log.w("BOOK", pricePerNight + "");
        Log.w("BOOK", totalDays + "");

        txtConfirmPrice.setText("$ " + String.format("%.2f", finalPrice));

        edtxtCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               String strCCNumber = edtxtCardNumber.getText().toString();
               String cardType = "";
               if (edtxtCardNumber.getText().toString().startsWith("4"))
                   imageViewCC.setImageResource(R.drawable.visa);
               else if (strCCNumber.startsWith("51") || strCCNumber.startsWith("52") ||
                       strCCNumber.startsWith("53") || strCCNumber.startsWith("54") ||
                       strCCNumber.startsWith("55"))
                   imageViewCC.setImageResource(R.drawable.mastercard);
               else if (strCCNumber.startsWith("34") || strCCNumber.startsWith("37"))
                   imageViewCC.setImageResource(R.drawable.amex);
               else
                   imageViewCC.setImageResource(R.drawable.ic_action_name);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

            btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String cardName = edtxtCardName.getText().toString();
                String cardNumber = edtxtCardNumber.getText().toString();
                String cardCVC = edtxtCardCVC.getText().toString();

                Log.w("BOOK", cardName);
                Log.w("BOOK", cardNumber);

                String cardExpireDate = numberPickerYearCC.getValue() + "-" +
                        String.format("%02d", numberPickerMonthCC.getValue() + 1) + "-01";

                Log.w("BOOK", cardExpireDate);

                if (cardName.isEmpty() || !Pattern.matches("[a-zA-Z\\h]+", cardName)) {
                    Toast.makeText(ActivityConfirmBooking.this, "Inform a Valid Name from your Credit Card", Toast.LENGTH_LONG).show();
                    return;
                }

                String cardType = "";
                if (cardNumber.startsWith("4"))
                    cardType = "Visa";
                else if (cardNumber.startsWith("51") || cardNumber.startsWith("52") ||
                        cardNumber.startsWith("53") || cardNumber.startsWith("54") ||
                        cardNumber.startsWith("55"))
                    cardType = "MasterCard";
                else if (cardNumber.startsWith("34") || cardNumber.startsWith("37"))
                    cardType = "American Express";
                else {
                    Toast.makeText(ActivityConfirmBooking.this, "This is not a Valid Credit Card \nAccepted Visa, Master and AMEX", Toast.LENGTH_LONG).show();
                    return;
                }

                if (cardNumber.length() != 16) {
                    Toast.makeText(ActivityConfirmBooking.this, "This is not a Valid Credit Card \nAccepted Visa, Master and AMEX", Toast.LENGTH_LONG).show();
                    return;
                }

                if (cardCVC.isEmpty() || cardCVC.length() != 3) {
                    Toast.makeText(ActivityConfirmBooking.this, "CV from your Credit Card not informed", Toast.LENGTH_LONG).show();
                    return;
                }

                int monthCC = numberPickerMonthCC.getValue() + 1;
                int yearCC = numberPickerYearCC.getValue();

                Calendar calToday = Calendar.getInstance();
                calToday.set(Calendar.DAY_OF_MONTH, 1);
                calToday.set(Calendar.HOUR_OF_DAY, 0);
                calToday.set(Calendar.MINUTE, 0);
                calToday.set(Calendar.SECOND, 0);
                calToday.set(Calendar.MILLISECOND, 0);

                Calendar calCard = Calendar.getInstance();
                calCard.set(yearCC,monthCC,1);
                calCard.set(Calendar.HOUR_OF_DAY, 0);
                calCard.set(Calendar.MINUTE, 0);
                calCard.set(Calendar.SECOND, 0);
                calCard.set(Calendar.MILLISECOND, 0);


                if ( calToday.compareTo(calCard) == 1 ) {
                    Toast.makeText(ActivityConfirmBooking.this,"Credit Card is expired",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Log.w("BOOK", cardType);

                try {
                    if (!sharedPref.contains("USER_EMAIL")) {
                        boolean value = false;
                        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                        intent.putExtra("key", value);
                        startActivityForResult(intent, 1);
                    }
                } catch (Exception ex) {
                    ex.getMessage();
                }

                int user_id = sharedPref.getInt("USER_ID", -1);
                String user_name = sharedPref.getString("USER_NAME", "");
                String hash_password = sharedPref.getString("HASH_PASSWORD", "");

                boolean authentication = true;

                if (authentication) {
                    boolean newBooking = CBooking.addBooking(dbHelper.getWritableDatabase(),
                            room.getPkRoom(), user_id, dateIn, dateOut, numAdults, numChildren,
                            "1", pricePerNight, cardName, cardType, cardNumber,
                            cardExpireDate, cardCVC);

                    if (newBooking) {
                        editor.putString("DATE_IN", dateIn);
                        editor.putString("DATE_OUT", dateOut);
                        editor.putInt("NUM_ROOMS", numRooms);
                        editor.putInt("NUM_ADULTS", numAdults);
                        editor.putInt("NUM_CHILDREN", numChildren);
                        editor.putString("CARD_NAME", cardName);
                        editor.putString("CARD_TYPE", cardType);
                        editor.putString("CARD_NUMBER", cardNumber);
                        editor.putString("CARD_EXPIRE_DATE", cardExpireDate);
                        editor.putString("CARD_CVC", cardCVC);
                        editor.putString("HOTEL_NAME", hotel.getName());
                        editor.putString("HOTEL_CITY", hotel.getCity());
                        editor.putString("ROOM_TYPE", room.getType());
                        editor.putFloat("TOTAL_PRICE", finalPrice);
                        editor.putInt("TOTAL_DAYS", totalDays);
                        editor.apply();

                        Intent intent = new Intent(ActivityConfirmBooking.this,
                                ActivityConfirmationView.class);
                        startActivity(intent);
                    } else {
/*
                        Toast.makeText(ActivityConfirmBooking.this,
                                "Sorry, a problem happened",
                                Toast.LENGTH_SHORT).show();
*/
                    }
                } else {
/*
                    Toast.makeText(ActivityConfirmBooking.this,
                            "Sorry, a critical problem happened",
                            Toast.LENGTH_SHORT).show();
*/
                }
            }
        });
    }
}
