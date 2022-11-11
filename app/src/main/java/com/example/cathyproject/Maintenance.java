package com.example.cathyproject;

import android.app.ProgressDialog;

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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.layout.*;

public class Maintenance extends Fragment  implements
        AdapterView.OnItemSelectedListener {




        TextInputEditText Location, Expenseamount,TripId;
        Button Submit;
        String location, expamount;
        ProgressDialog pdDialog;
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
                    String sql="INSERT INTO expenses (Expensename,Expenseamount,Location) VALUES ('"+Expenseamount.getText().toString()+"','"+Location.getText().toString()+"');";


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
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(getContext(),String.valueOf( position), Toast.LENGTH_SHORT).show();
        selectedidpos=position;
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}

