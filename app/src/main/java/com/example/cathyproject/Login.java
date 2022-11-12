package com.example.cathyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    TextView Registernow;
    ProgressDialog pdDialog;
    String lEmail, lPass;
    TextInputEditText Email, Password;
    Button loginButton;
    String is_signed_in = "";
    SharedPreferences mPreferences;
    String sharedprofFile = "MySharedPref";
   SharedPreferences.Editor preferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPreferences = getSharedPreferences(sharedprofFile, MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();

        is_signed_in = mPreferences.getString("issignedin", "false");



        Registernow = (TextView) findViewById(R.id.registernow);
        pdDialog = new ProgressDialog(Login.this);
        pdDialog.setTitle("Login please wait...");
        pdDialog.setCancelable(false);



        Email= (TextInputEditText) findViewById(R.id.lEmail);
        Password = (TextInputEditText) findViewById(R.id.lPassword);
        loginButton = (Button) findViewById(R.id.loginbutton);
        Registernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this, Registration.class);
                startActivity(register);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lEmail = Email.getText().toString().trim();
                lPass = Password.getText().toString().trim();
                if (lEmail.isEmpty() || lPass.isEmpty()) {
                    Toast.makeText(Login.this, "please enter valid data", Toast.LENGTH_SHORT).show();
                } else {
                    Login();
                }
            }
        });
    }

    private void Login() {
        pdDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("What", response);
                        pdDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            String message = jsonObject.getString("message");
                            String id = jsonObject.getString("UserId");
                            String FirstName = jsonObject.getString("FirstName");
                            String LastName = jsonObject.getString("LastName");
                            String PhoneNumber = jsonObject.getString("PhoneNumber");
                            String Role = jsonObject.getString("Role");

                            if (success.equals("1")) {
                                Toast.makeText(getApplicationContext(), "Logged In  Success", Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();

                                preferencesEditor.putString("issignedin", "true");
                                preferencesEditor.putString("UserId", id);
                                preferencesEditor.putString("FirstName", FirstName);
                                preferencesEditor.putString("LastName", LastName);
                                preferencesEditor.putString("PhoneNumber", PhoneNumber);
                                preferencesEditor.putString("Role", Role);
                                preferencesEditor.apply();

                                Intent i = new Intent(Login.this, Booking.class);
                                startActivity(i);
                                finish();

                            }
                            if (Role.equals("Customer")) {
                                Intent intent = new Intent(Login.this, Booking.class);
                                intent.putExtra(id, id);
                                intent.putExtra(FirstName, FirstName);
                                intent.putExtra(LastName, LastName);

                                finish();
                                startActivity(intent);
                            } if (Role.equals("Driver")) {
                                Intent intent = new Intent(Login.this, Driver.class);
                                intent.putExtra(id, id);
                                intent.putExtra(FirstName, FirstName);
                                intent.putExtra(LastName, LastName);
                                finish();
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                pdDialog.dismiss();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"Login Error !1"+e,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pdDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Login Error !2" + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("Email", lEmail);
                params.put("Password", lPass);
                params.put("action", "login");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}