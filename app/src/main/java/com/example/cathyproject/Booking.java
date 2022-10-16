package com.example.cathyproject;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Booking extends AppCompatActivity implements TripsAdapter.OnNoteListener   {

    ProgressDialog pdDialog;
    List<Trips> tripsList = new ArrayList<>();
    private RecyclerView rv_trips;
    private static final String TAG = "Booking";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        tripsList = new ArrayList<>();
        rv_trips = findViewById(R.id.rv_trips);
        rv_trips.setLayoutManager(new LinearLayoutManager(this));



        pdDialog= new ProgressDialog(Booking.this);
        pdDialog.setTitle("loading...");
        pdDialog.setCancelable(false);


        selectdata();

    }

    public void selectdata() {
        pdDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("What", response);
                        pdDialog.dismiss();



                        try {
                            JSONObject eventobject = new JSONObject(response);

                            JSONArray array = eventobject.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                // creating a new json object and
                                // getting each object from our json array.

                                // we are getting each json object.
                                JSONObject responseObj = array.getJSONObject(i);

                                String destid = responseObj.getString("DestinationId");
                                String destname = responseObj.getString("Tripname");
                                String destamount = responseObj.getString("Amount");


                                tripsList.add(new Trips(destid, destname, destamount));
                                rv_trips.setAdapter(new TripsAdapter(tripsList, Booking.this));


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
                String sql = "SELECT tripdetails.DestinationId, destination.Tripname, tripdetails.Amount from tripdetails inner join destination on destination.DestinationId=tripdetails.DestinationId";
                params.put("sql", sql);
                params.put("action", "getdata");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    @Override
    public void onNoteClick(int position) {

        Intent intent = new Intent(Booking.this, Paying.class);
        intent.putExtra("Id",  tripsList.get(position).getDestinationId());
        intent.putExtra("Amount",  tripsList.get(position).getAmount());
        startActivity(intent);
    


        Toast.makeText(getApplicationContext(), String.valueOf(tripsList.get(position).getTripname()
), Toast.LENGTH_SHORT).show();
    }
}