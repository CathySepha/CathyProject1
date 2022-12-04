package com.example.cathyproject;

import android.app.ProgressDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Spinner;

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

import static android.R.layout.*;
import static android.content.Context.MODE_PRIVATE;

public class Maintenance extends Fragment  implements
        AdapterView.OnItemSelectedListener {




        TextInputEditText Location, Expenseamount,TripId;
        Button Submit;
        String location, expamount,item,tripid,UserId;
        ProgressDialog pdDialog;
    SharedPreferences.Editor preferencesEditor;
    SharedPreferences mPreferences;
        Spinner spinn;
        public int selectedidpos;







        public Maintenance() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_maintenance, container, false);


            // Inflate the layout for this fragment
            Expenseamount=view.findViewById(R.id.expamount);
            Location=view.findViewById(R.id.location);
            mPreferences= this.getActivity().getSharedPreferences("sharedprofFile", MODE_PRIVATE);
            preferencesEditor = mPreferences.edit();
            SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);

            UserId= sh.getString("UserId", "");



            pdDialog= new ProgressDialog(getContext());
            pdDialog.setTitle("loading");
            pdDialog.setCancelable(false);

            spinn = view.findViewById(R.id.spinner);
            spinn.setOnItemSelectedListener(this);
            // Spinner Drop down elements
            List<String> Expensename = new ArrayList<String>();
            Expensename.add("Fuelling");
            Expensename.add("Servicing");
            // Creating adapter for spinner
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item,Expensename);
// Drop down layout style - list view with radio button
            dataAdapter.setDropDownViewResource(simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            spinn.setAdapter(dataAdapter);


            Submit =view.findViewById(R.id.save);
            Submit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)

                {
                    insertexpenses();
                }
            });
            selectdata();



            return view;
        }

        public void  insertexpenses()
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
                                    Toast.makeText(getContext(),"Data inserted successfully",Toast.LENGTH_LONG).show();
                                    pdDialog.dismiss();

                                   Expenseamount.setText("");
                                   Location.setText("");



                                }
                                if(success.equals("0")){
                                    Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                                    pdDialog.dismiss();
                                }
                                if(success.equals("3")){
                                    Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
                                    pdDialog.dismiss();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(),"Insertion Error !1"+e,Toast.LENGTH_LONG).show();
                            }
                        }
                    } ,new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pdDialog.dismiss();
                    Log.d("volley error", error.toString());
                    Toast.makeText(getContext(),"Insertion Error !2"+error,Toast.LENGTH_LONG).show();
                }
            })
            {
                @Override
                protected Map<String, String> getParams() {
                    Map<String,String> params = new HashMap<>();
                    String sql="INSERT INTO expenses (TripId,Expensename,Expenseamount,Location) VALUES ('"+tripid+"','"+item+"','"+Expenseamount.getText().toString()+"','"+Location.getText().toString()+"');";


                    params.put("sql",sql);

                    params.put("action","insertdata");


                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            requestQueue.add(stringRequest);
        } //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
       item = parent.getItemAtPosition(position).toString();

        selectedidpos=position;
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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






                                tripid=responseObj.getString("TripId");
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
                Toast.makeText(getContext(), "Insertion Error !2" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                String sql = "SELECT bookings.TripId, bookings.Dropoffstage, trips.UserId, bookings.Status FROM bookings INNER JOIN trips ON trips.TripId=bookings.TripId where bookings.Status='Active' and trips.UserId='"+UserId+"'";
                params.put("sql", sql);
                params.put("action", "getdata");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}

