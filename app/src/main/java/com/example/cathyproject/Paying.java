package com.example.cathyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class Paying extends AppCompatActivity {
    TextView receiver_msg;

    String lNoOfseats, lDropoff;
    TextInputEditText Dropoffstage, NoOfSeats;
    Button PAY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying);
        receiver_msg = findViewById(R.id.lAmount);


        Intent intent = getIntent();
        String str = intent.getStringExtra("Amount");
        receiver_msg.setText(str);

        Dropoffstage= (TextInputEditText) findViewById(R.id.lDropoff);
        NoOfSeats = (TextInputEditText) findViewById(R.id.lNoOfseats);
        PAY = (Button) findViewById(R.id.lpay);
        PAY.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
                open();
            }
        });
    }
    public void open(){}
}