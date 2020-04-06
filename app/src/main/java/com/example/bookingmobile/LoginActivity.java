package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private Class activity =   MainActivity.class;
    private boolean value =   true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_login);



       Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getBoolean("key");
            if(!value){
                activity =  ActivityConfirmBooking.class;
            }
        }

       // References
        final EditText email = findViewById(R.id.txt_Email);

        final EditText password = findViewById(R.id.txt_Password);

        Button btnLogin = findViewById(R.id.btn_Login);
        Button btnLoginLater = findViewById(R.id.btn_Later);
        if(!value){
            btnLoginLater.setVisibility(View.INVISIBLE);
        }
        Button btnRegister = findViewById(R.id.btn_Register);

        // Shared Preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPref.contains("USER_EMAIL")){
            email.setText( sharedPref.getString("USER_EMAIL",""));
        }
        editor = sharedPref.edit();

        // Create the object and database connection
        final CSQLiteHelper dbHelper = new CSQLiteHelper((getApplicationContext()));
        dbHelper.createDatabaseConnection();

        //Login Later
        btnLoginLater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.w("Login", "Login Later");
                String clShared = "";
                editor.clear();
                editor.commit();
                try{
                    if (sharedPref.contains("USER_EMAIL")){
                        clShared = sharedPref.getString("USER_EMAIL","");
                        Log.w("Login", "Login Later - Unable to clear shared prefs");
                    }
                }
                catch(Exception ex){
                    ex.getMessage();
                }
                Log.w("Login", "Shared prefs clear " + clShared);
                Intent intent = new Intent(getBaseContext(), activity);
                startActivity(intent);
            }
        });
        // Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailContent = email.getText().toString();
                String passwordContent = password.getText().toString();

                if(TextUtils.isEmpty(emailContent) || TextUtils.isEmpty(passwordContent)){
                    Toast.makeText(getBaseContext(), "Please provide email address and/or password.", Toast.LENGTH_SHORT).show();
                }
                else if (CUser.checkAuthentication(dbHelper.getReadableDatabase(),emailContent, passwordContent)){
                    Log.w("Login", "Verified user - " + emailContent);
                    CUser currentUser = CUser.getAllDataFromUser(dbHelper.getReadableDatabase(),emailContent, passwordContent);
                    try{
                        editor.putString("USER_EMAIL", emailContent);
                        editor.putString("HASH_PASSWORD", passwordContent);
                        editor.putInt("USER_ID", currentUser.getPkUser());
                        editor.putInt("ROOM_SELECTED", -1);
                        editor.putString("HOTEL_NAME", "");
                        editor.putString("HOTEL_CITY", "");
                        editor.putString("ROOM_TYPE", "");
                        editor.putString("DATE_IN", "");
                        editor.putString("DATE_OUT", "");
                        editor.putInt("TOTAL_DAYS", -1);
                        editor.putFloat("TOTAL_PRICE", 0);
                        editor.putInt("NUM_ROOMS", 1);
                        editor.putInt("NUM_ADULTS", 1);
                        editor.putInt("NUM_CHILDREN", 0);
                        editor.putString("CARD_NAME", "");
                        editor.putString("CARD_TYPE", "");
                        editor.putString("CARD_NUMBER", "");
                        editor.putString("CARD_EXPIRE_DATE", "");
                        editor.putString("CARD_CVC", "");
                    }
                    catch(Exception ex){
                        ex.getMessage();
                    }

                    editor.apply();
                    Log.w("Login", "Verified user - " + emailContent + currentUser.getPkUser());
                    Intent intent = new Intent(getBaseContext(), activity);
                    intent.putExtra("key",value);
                    if(activity == MainActivity.class){
                        startActivity(intent);
                    }
                    else {
                        setResult(1, intent);
                        finish();
                    }
                }
                else{
                    email.setText("");
                    password.setText("");
                    Toast.makeText(getBaseContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                    Log.w("Login", "Invalid user - " + emailContent);
                }
            }
        });
        // Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailContent = email.getText().toString();
                String passwordContent = password.getText().toString();
                int pk = 0;
                if(TextUtils.isEmpty(emailContent) || TextUtils.isEmpty(passwordContent)){
                    Toast.makeText(getBaseContext(), "Please provide email address and password.", Toast.LENGTH_SHORT).show();
                }
                else if (!CUser.checkAuthentication(dbHelper.getReadableDatabase(),emailContent, passwordContent)){
                    CUser newUser = CUser.addUser(dbHelper.getWritableDatabase(),emailContent,passwordContent, emailContent);
                    try{
                        pk = newUser.getPkUser();
                    }
                    catch(Exception ex){
                        ex.getMessage();
                    }
                    Log.w("Login", "New user - " + emailContent);
                    editor.putString("USER_EMAIL", emailContent);
                    editor.putInt("USER_ID", pk);
                    editor.putString("HASH_PASSWORD", passwordContent);
                    editor.apply();
                    Intent intent = new Intent(getBaseContext(), activity);
                    intent.putExtra("key",value);
                    if(activity == MainActivity.class){
                        startActivity(intent);
                    }
                    else {
                        setResult(1, intent);
                        finish();
                    }

                }
                else{
                    Log.w("Login", "User already exists, redirected to login - " + emailContent);
                    editor.putString("USER_EMAIL", emailContent);
                    editor.commit();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });




    }
}
