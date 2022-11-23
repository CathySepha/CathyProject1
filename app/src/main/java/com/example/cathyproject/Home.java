package com.example.cathyproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class Home extends Fragment implements DropoffAdapter.OnNoteListener   {
    ProgressDialog pdDialog;
    ListView dropoffnames;

    List<String> dropoflist = new ArrayList<String>();
    List<Dropoffs> dropoffsList = new ArrayList<>();
    DropoffAdapter adapter;

    private RecyclerView rv_dropoff;
    SharedPreferences.Editor preferencesEditor;
    SharedPreferences mPreferences;
    String sharedprofFile = "MySharedPref";
    String PhoneNumber,UserId,tripid;
    Button close;


    // Make sure to use the FloatingActionButton for all the FABs
    FloatingActionButton mLogout;
    // These are taken to make visible and invisible along with FABs
    TextView addlogouttext;
    // to check whether sub FAB buttons are visible or not.
    Boolean isAllFabsVisible;


    public Home() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        mPreferences= this.getActivity().getSharedPreferences("sharedprofFile", MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();
        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);

        PhoneNumber = sh.getString("PhoneNumber", "");
        close=view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closetrips();
            }
        });

        UserId= sh.getString("UserId", "");

        // Register all the FABs with their IDs This FAB button is the Parent
        // FAB button
        mLogout = view.findViewById(R.id.logoutfab);
        // Also register the action name text, of all the FABs.
        addlogouttext = view.findViewById(R.id.logouttext);
        // Now set all the FABs and all the action name texts as GONE

        isAllFabsVisible = true;
        // making the fab and action text visible when the parent FAB is clicked
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferencesEditor.putString("issignedin", "false");
                Intent i=new Intent(getContext(),Login.class);
                //Intent is used to switch from one activity to another.
                startActivity(i);
                //invoke the SecondActivity.

            }
        });

        pdDialog= new ProgressDialog(getContext());
        pdDialog.setTitle("loading...");
        pdDialog.setCancelable(false);
        dropoffsList = new ArrayList<>();
        adapter=new DropoffAdapter( dropoffsList);


        dropoffnames = view.findViewById(R.id.rv_dropoff);

        selectdata();



        return view;

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





                               dropoflist.add(responseObj.getString("Dropoffstage"));
                               tripid=responseObj.getString("TripId");
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                        ArrayAdapter<String> arr;
                        arr
                                = new ArrayAdapter<String>(
                                getContext(),
                                R.layout.support_simple_spinner_dropdown_item,
                                dropoflist);
                        dropoffnames.setAdapter(arr);
                        dropoffnames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Toast.makeText(getContext(), "click"+i, Toast.LENGTH_SHORT).show();
                            }
                        });

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





    @Override
    public void onNoteClick(int position) {

    }




    public void closetrips() {
        pdDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Bpink", response);
                        pdDialog.dismiss();
                        closebookings();



                        try {
                            JSONObject eventobject = new JSONObject(response);

                            JSONArray array = eventobject.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                // creating a new json object and
                                // getting each object from our json array.

                                // we are getting each json object.
                                JSONObject responseObj = array.getJSONObject(i);


                                String Remslots = responseObj.getString("Remainingslots");


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
                String sql = "Update trips set trips.Status='Closed' where TripId='"+tripid+"'";
                params.put("sql", sql);
                params.put("action", "insertdata");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

// code for closing booking after the trip is closed

    public void closebookings() {
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
                String sql = "Update bookings set bookings.Status='Closed' where TripId='"+tripid+"'";
                params.put("sql", sql);
                params.put("action", "insertdata");


                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}