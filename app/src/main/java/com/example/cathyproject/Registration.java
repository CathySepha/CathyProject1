package com.example.cathyproject;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import java.util.Random;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextInputEditText  Firstname, Lastname, Email,Phone,Password;
    Button rregister ;
    String sfirstname,slastname,semail,sphonenumber, spassword, srole;
    ProgressDialog pdDialog;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Firstname=(TextInputEditText)findViewById(R.id.rfname);
        Lastname=(TextInputEditText)findViewById(R.id.rlname);
        Email =(TextInputEditText)findViewById(R.id.remail);
        Phone=(TextInputEditText)findViewById(R.id.rnumber);
        Password=(TextInputEditText)findViewById(R.id.rpassword);
        rregister=(Button) findViewById(R.id.rregister);



         spinner = (Spinner) findViewById(R.id.spinner);
        // Spinner click listener
               spinner.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        List<String> Role = new ArrayList<String>();
        Role.add("Customer");
        Role.add("Driver");
    // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Role);
// Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        pdDialog= new ProgressDialog(Registration.this);
        pdDialog.setTitle("Registering please wait...");
        pdDialog.setCancelable(false);


        rregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfirstname=Firstname.getText().toString().trim();
                slastname=Lastname.getText().toString().trim();
                semail=Email.getText().toString().trim();
                sphonenumber=Phone.getText().toString().trim();
                spassword=Password.getText().toString().trim();
                srole=spinner.getSelectedItem().toString();


                if(sfirstname.isEmpty()||slastname.isEmpty()||semail.isEmpty()||sphonenumber.isEmpty()||spassword.isEmpty())
                {
                    Toast.makeText(Registration.this,"please enter valid data",Toast.LENGTH_SHORT).show();
                }
                else{
                    Register();
                }
            }
        });
    }

    private void Register()
    {
        pdDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("anyText",response);
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");

                            if(success.equals("1")){
                                Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();

                                Intent Login = new Intent(Registration.this,Login.class);
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
                            Toast.makeText(getApplicationContext(),"Registration Error !1"+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
                Log.d("volley error", error.toString());
                Toast.makeText(getApplicationContext(),"Registration Error !2"+error,Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("FirstName",sfirstname);
                params.put("LastName",slastname);
                params.put("Email",semail);
                params.put("PhoneNumber",sphonenumber);
                params.put("Role",srole);
                params.put("Password",spassword);
                params.put("action","registration");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}