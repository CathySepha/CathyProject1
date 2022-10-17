package com.example.cathyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class Paying extends AppCompatActivity {
    TextView receiver_msg;

    String pNoOfseats, pDropoffstage;
    TextInputEditText Dropoffstage, NoOfSeats;
    Button PAY;
    float res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying);
        receiver_msg = findViewById(R.id.lAmount);


        Intent intent = getIntent();
        String str = intent.getStringExtra("Amount");
        receiver_msg.setText(str);

        Dropoffstage= (TextInputEditText) findViewById(R.id.pDropoffstage);
        NoOfSeats = (TextInputEditText) findViewById(R.id.pNoOfseats);
        PAY = (Button) findViewById(R.id.lpay);
        PAY.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // your handler code here

                    String Seats=NoOfSeats.getText().toString();
                    float mnum1=Float.parseFloat(Seats);

                    String Amount=receiver_msg.getText().toString();
                    float mnum2=Float.parseFloat(Amount);
                         res=mnum1*mnum2;
                receiver_msg.setText(String.valueOf(res));
                open();
            }
        });
    }
    public void open(float res){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}