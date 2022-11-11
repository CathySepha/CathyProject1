package com.example.cathyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Paying extends AppCompatActivity {
    TextView receiver_msg;

    String pNoOfseats, pDropoffstage,UserId,TripId, Amount,PhoneNumber;
    TextInputEditText Dropoffstage, NoOfSeats;
    TextView RemainingSits;
    Button PAY;
    float res;
    ProgressDialog pdDialog;

    List<String> destinationId,Remainingslots;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paying);
        receiver_msg = findViewById(R.id.lAmount);

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);

        PhoneNumber = sh.getString("PhoneNumber", "");

        UserId= sh.getString("UserId", "");
        RemainingSits=findViewById(R.id.pslots);

        Intent intent = getIntent();
        TripId= intent.getStringExtra("TripId");
        Amount = intent.getStringExtra("Amount");
        receiver_msg.setText(Amount);

        pdDialog= new ProgressDialog(Paying.this);
        pdDialog.setTitle("loading...");
        pdDialog.setCancelable(false);

       destinationId= new ArrayList<String>();
        Remainingslots= new ArrayList<String>();


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
        selectdata();

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
    public void selectdata() {
        pdDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Bpink", response);
                        pdDialog.dismiss();



                        try {
                            JSONObject eventobject = new JSONObject(response);

                            JSONArray array = eventobject.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                // creating a new json object and
                                // getting each object from our json array.

                                // we are getting each json object.
                                JSONObject responseObj = array.getJSONObject(i);


                                String Remslots = responseObj.getString("Remainingslots");

                                RemainingSits.setText(Remslots);




                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
                Log.d("volley error", error.toString());
                Toast.makeText(getApplicationContext(), "Insertion Error !2" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String sql = "SELECT Remainingslots FROM trips WHERE TripId = '"+TripId+"' AND Status = 'Active'";
                params.put("sql", sql);
                params.put("action", "getdata");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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
                            mpesa();


                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),"Details added Successfully",Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();

//                                Intent Login = new Intent(Paying.this,Paying.class);
//                                startActivity(Login);
//                                finish();

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
                String sql="INSERT INTO bookings (UserId, TripId, Amount, NoOfSeats, Dropoffstage,PhoneNumber) VALUES ('"+UserId+"','"+TripId+"','"+Integer.parseInt(Amount) * Integer.parseInt(pNoOfseats)+"','"+pNoOfseats+"','"+pDropoffstage+"','"+PhoneNumber+"');";
               Log.d("Moi",sql);
                params.put("sql", sql);
                params.put("action","insertdata");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    // VOLLEY FOR SMS //
    public void messaging() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.smsurl),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Bpink", response);




                        try {
                            JSONObject eventobject = new JSONObject(response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("volley error", error.toString());
                Toast.makeText(getApplicationContext(), "Insertion Error !2" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Phone",PhoneNumber);
                params.put("Message","Booking Confirmed");




                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    // VOLLEY FOR MPESA
    public void mpesa() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.mpesaurl),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Jijo", response);




                        try {
                            messaging();
                            JSONObject eventobject = new JSONObject(response);

                            JSONArray array = eventobject.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                // creating a new json object and
                                // getting each object from our json array.

                                // we are getting each json object.
                                JSONObject responseObj = array.getJSONObject(i);







                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("volley error", error.toString());
                Toast.makeText(getApplicationContext(), "Insertion Error !2" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("Phone", PhoneNumber);



                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}