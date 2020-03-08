package com.example.bookingmobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<CUser> lstUser = new ArrayList<CUser>();
    CSQLiteHelper dbHelper;
    Button btnGetData;
    Button btnCheckUser;
    LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetData = findViewById(R.id.btnGetData);
        container = findViewById(R.id.container);

        dbHelper = new CSQLiteHelper((getApplicationContext()));
        dbHelper.createDatabase();

        btnGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lstUser = dbHelper.getAllUsers();

                for (CUser cuser:lstUser) {
                    LayoutInflater inflater = (LayoutInflater) getBaseContext().
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    View addView = inflater.inflate(R.layout.row, null);

                    TextView txtLoginName = addView.findViewById(R.id.txt1);
                    TextView txtHashPassword = addView.findViewById(R.id.txt2);
                    TextView txtEmail = addView.findViewById(R.id.txt3);

                    txtLoginName.setText(cuser.getLoginName());
                    txtHashPassword.setText(cuser.getHashPassword());
                    txtEmail.setText(cuser.getEmail());

                    container.addView(addView);
                }
            }
        });

        btnCheckUser = findViewById(R.id.btnCheckUser);
        btnCheckUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dbHelper.checkUser("User1", "pass1"))
                    Toast.makeText(MainActivity.this, "User and Password OK!!",
                            Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MainActivity.this, "User and Password NOT OK!!",
                            Toast.LENGTH_SHORT).show();
            }
        });

    }
}
