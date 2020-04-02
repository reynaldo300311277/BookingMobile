package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityConfirmBooking extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private CHotel hotel;
    private CRoom room;

    private EditText edtxtCardName;
    private EditText edtxtCardNumber;
    private EditText edtxtCardExpireDate;
    private EditText edtxtCardCVC;

    private String dateIn;
    private String dateOut;
    private int numRooms;
    private int numAdults;
    private int numChildren;
    private int roomSelected;
    private int totalDays;

    float pricePerNight;

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
        totalDays = 0;

        Intent intent = getIntent();
        this.hotel = intent.getParcelableExtra("CHOTEL");

        room = hotel.getArrayRooms().get(roomSelected);

        TextView txtConfirmHotel = findViewById(R.id.txtConfirmHotel);
        TextView txtConfirmCity = findViewById(R.id.txtConfirmCity);
        TextView txtConfirmRoomType = findViewById(R.id.txtConfirmRoomType);
        TextView txtConfirmNumbers = findViewById(R.id.txtConfirmNumbers);
        TextView txtConfirmDateIn = findViewById(R.id.txtConfirmDateIn);
        TextView txtConfirmDateOut = findViewById(R.id.txtConfirmDateOut);
        TextView txtConfirmPrice = findViewById(R.id.txtConfirmPrice);

        SimpleDateFormat sdfYmd = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date dIn = sdfYmd.parse(dateIn);
            Date dOut = sdfYmd.parse(dateOut);
            long diff = dOut.getTime() - dIn.getTime();
            totalDays = Math.round(diff / (24 * 60 * 60 * 1000));
        } catch (ParseException ex) {
        }

        txtConfirmHotel.setText(hotel.getName());
        txtConfirmCity.setText(hotel.getCity());
        txtConfirmRoomType.setText(room.getType());
        txtConfirmNumbers.setText("#Rooms: " + numRooms + "\n#Adults: " + numAdults + "\n#Children: " + numChildren);
        txtConfirmDateIn.setText(dateIn);
        txtConfirmDateOut.setText(dateOut);

        pricePerNight = room.getPrice();
        final float finalPrice = pricePerNight * totalDays;

        txtConfirmPrice.setText("$ " + String.format("%.2f", finalPrice));

        Button btnConfirmBooking = findViewById(R.id.btnConfirmBooking);
        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edtxtCardName = findViewById(R.id.edtxtCardName);
                edtxtCardNumber = findViewById(R.id.edtxtCardNumber);
                edtxtCardExpireDate = findViewById(R.id.edtxtExpireDate);
                edtxtCardCVC = findViewById(R.id.edtxtCardCVC);

                String cardName = edtxtCardName.getText().toString();
                String cardNumber = edtxtCardNumber.getText().toString();
                String cardExpireDate = edtxtCardExpireDate.getText().toString();
                String cardCVC = edtxtCardCVC.getText().toString();

                // check if these values were filled
                if (dateIn.isEmpty() || dateOut.isEmpty() || cardName.isEmpty() ||
                        cardNumber.isEmpty() || cardExpireDate.isEmpty() || cardCVC.isEmpty())
                {
                    Toast.makeText(ActivityConfirmBooking.this,
                            "Plase, fill the required information",
                            Toast.LENGTH_SHORT).show();
                }
                // check if these values are positives and less than the threshold
                else if (numAdults < 0 || numChildren < 0 || pricePerNight < 0 ||
                        numAdults > 4 || numChildren > 4)
                {
                    Toast.makeText(ActivityConfirmBooking.this,
                            "Information invalid, please review booking information",
                            Toast.LENGTH_SHORT).show();
                }
                // check if the format of check-in/check-out dates are corrects
                else if (!isDateValid(dateIn, "yyyy-MM-dd") ||
                        !isDateValid(dateOut, "yyyy-MM-dd"))
                {
                    Toast.makeText(ActivityConfirmBooking.this,
                            "Date Format invalid or inexistent",
                            Toast.LENGTH_SHORT).show();
                }
                // check if the format of expire date is correct
                else if (!isDateValid(cardExpireDate, "MM/yy"))
                {
                    Toast.makeText(ActivityConfirmBooking.this,
                            "Expire Date Format invalid",
                            Toast.LENGTH_SHORT).show();
                }
                // check if check-in date is before the check-out
                else if (dateIn.compareTo(dateOut) >= 0)
                {
                    Toast.makeText(ActivityConfirmBooking.this,
                            "Checkout date must be after than checkin",
                            Toast.LENGTH_SHORT).show();
                }
                // check if the credit card and cvc are numerics
                else if (!isNumeric(cardNumber) || !isNumeric(cardCVC))
                {
                    Toast.makeText(ActivityConfirmBooking.this,
                            "Invalid numeric information",
                            Toast.LENGTH_SHORT).show();
                }
                // check if the credicard number has 16 digits
                else if (cardNumber.length() != 16)
                {
                    Toast.makeText(ActivityConfirmBooking.this,
                            "Credit Card Number incomplete",
                            Toast.LENGTH_SHORT).show();
                }
                // check if the credicard CVC has 3  digits
                else if (cardCVC.length() != 3)
                {
                    Toast.makeText(ActivityConfirmBooking.this,
                            "Credit Card CVC incomplete",
                            Toast.LENGTH_SHORT).show();
                }
                // check if the type of credit card is VISA, MASTERCARD or AMERICAN EXPRESS
                else if (!(cardNumber.startsWith("4") || cardNumber.startsWith("51") ||
                        cardNumber.startsWith("52") ||  cardNumber.startsWith("53") ||
                        cardNumber.startsWith("54") ||  cardNumber.startsWith("55") ||
                        cardNumber.startsWith("34") || cardNumber.startsWith("37")))
                {
                    Toast.makeText(ActivityConfirmBooking.this,
                            "Credit Card Number invalid",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String cardType = "";

                    if (cardNumber.startsWith("4"))
                        cardType = "Visa";
                    else if (cardNumber.startsWith("51") || cardNumber.startsWith("52") ||
                            cardNumber.startsWith("53") || cardNumber.startsWith("54") ||
                            cardNumber.startsWith("55"))
                        cardType = "MasterCard";
                    else if (cardNumber.startsWith("34") || cardNumber.startsWith("37"))
                        cardType = "American Express";

                    SimpleDateFormat sdfMy = new SimpleDateFormat("MM/yy");

                    try {
                        Date dExpireDate = sdfMy.parse(cardExpireDate);
                        SimpleDateFormat sdfyM = new SimpleDateFormat("MM/yyyy");
                        cardExpireDate = sdfyM.format(dExpireDate);
                    }
                    catch (ParseException ex) {
                    }

                    // ****************************************************************************
                    // user is set to '1' because we do not have authentication - VER MAIN ACTIVITY
                    // ****************************************************************************
                    int user_id = sharedPref.getInt("USER_ID", -1);
                    String user_name = sharedPref.getString("USER_NAME", "");
                    String hash_password = sharedPref.getString("HASH_PASSWORD", "");

                    // *************************************
                    // aqui vem o método de autenticação!!!!
                    // *************************************
                    boolean authentication = true;

                    if (authentication)
                    {
                        boolean newBooking = CBooking.addBooking(dbHelper.getWritableDatabase(),
                                room.getPkRoom(), user_id, dateIn, dateOut, numAdults, numChildren,
                                "1",  pricePerNight, cardName, cardType, cardNumber,
                                cardExpireDate, cardCVC);

                        if (newBooking)
                        {
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
                        }
                        else {
                            Toast.makeText(ActivityConfirmBooking.this,
                                    "Sorry, a problem happened",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        // call the authentication form and pass the values to include the booking there
                        // that is why I decide to check the authentication here, because all values
                        // were validated, excepted the user
                    }
                }
            }
        });
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        }
        catch (NumberFormatException ex){
            return false;
        }
    }

    private static boolean isDateValid(String dateToValidate, String dateFromat){

        if(dateToValidate == null) { return false;  }

        SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
        sdf.setLenient(false);

        try { // if not valid, it will throw ParseException
            Date date = sdf.parse(dateToValidate);
        }
        catch (ParseException e) {
            return false;
        }

        return true;
    }
}
