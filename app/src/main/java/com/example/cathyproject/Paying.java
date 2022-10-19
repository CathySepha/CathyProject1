package com.example.cathyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Paying extends AppCompatActivity {
    TextView receiver_msg;

    String pNoOfseats, pDropoffstage, str;
    TextInputEditText Dropoffstage, NoOfSeats;
    Button PAY;
    float res;
    ProgressDialog pdDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying);
        receiver_msg = findViewById(R.id.lAmount);


        Intent intent = getIntent();
        String str = intent.getStringExtra("Amount");
        receiver_msg.setText(str);

        pdDialog= new ProgressDialog(Paying.this);
        pdDialog.setTitle("loading...");
        pdDialog.setCancelable(false);

        Dropoffstage= (TextInputEditText) findViewById(R.id.pDropoffstage);
        NoOfSeats = (TextInputEditText) findViewById(R.id.pNoOfseats);
        PAY = (Button) findViewById(R.id.lpay);
        PAY.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pDropoffstage=Dropoffstage.getText().toString().trim();
                pNoOfseats=NoOfSeats.getText().toString().trim();
                // your handler code here

                    String Seats=NoOfSeats.getText().toString();
                    float mnum1=Float.parseFloat(Seats);

                    String Amount=receiver_msg.getText().toString();
                    float mnum2=Float.parseFloat(Amount);
                         res=mnum1*mnum2;
                receiver_msg.setText(String.valueOf(res));
                open(res);

            }
        });
    }
    public void open(float res){
        String str = String.valueOf(res);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Confirm payment of Shillings "+str);
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(Paying.this,"You clicked yes",Toast.LENGTH_LONG).show();
                        paying();
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
    private void paying()
    {
        pdDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url),

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pdDialog.hide();

                        Log.d("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),"Details added Successfully",Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();

                                Intent Login = new Intent(Paying.this,Paying.class);
                                startActivity(Login);
                                finish();

                            }
                            if(success.equals("0")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                            }
                            if(success.equals("3")){
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Insertion Error !1"+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
                Log.d("volley error", error.toString());
                Toast.makeText(getApplicationContext(),"Insertion Error !2"+error,Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();
                String sql="INSERT INTO bookings (NoOfSeats, Dropoffstage) VALUES ('$pNoOfseats', '$pDropoffstage');";

                params.put("NoOfSeats", pNoOfseats);
                params.put("Dropoffstage",pDropoffstage);
                params.put("action","insertdata");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}