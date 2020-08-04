package com.example.otplogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText phoneNumber;
    EditText otp;
    Button sendOtp;
    Button verifyOtp;
    String details;
    String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        phoneNumber = (EditText)findViewById(R.id.editText_phone);
        sendOtp = (Button)findViewById(R.id.submit_phone_btn);





        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RequestQueue requestQueue;
                requestQueue = Volley.newRequestQueue(MainActivity.this);


                String url = "https://2factor.in/API/V1/{Api Key}/SMS/+91"+phoneNumber.getText().toString().trim()+"/AUTOGEN";
                Log.d("myApp", url);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    details = response.getString("Details");
                                    status = response.getString("Status");
                                    Log.d("myApp", "The session id is " + details + status);

                                    if(status == "Success"){
                                        Toast.makeText(MainActivity.this, "Otp Sent", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("myApp", "Something went wrong");
                    }
                });

                requestQueue.add(jsonObjectRequest);

            }
        });

        otp = (EditText)findViewById(R.id.editText_otp);
        verifyOtp = (Button)findViewById(R.id.verify_phone_btn);

        verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, details, Toast.LENGTH_SHORT).show();
                RequestQueue requestQueue;
                requestQueue = Volley.newRequestQueue(MainActivity.this);
                String url2 = "https://2factor.in/API/V1/{Api Key}/SMS/VERIFY/"+details+"/"+otp.getText().toString().trim();
                Log.d("myApp", url2);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        url2,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.d("myApp", "The response id is " + response.getString("Status"));
                                    Log.d("myApp", "Details :  " + response.getString("Details"));


                                    if(response.getString("Status").toString() == "Success"){
                                        Toast.makeText(MainActivity.this, "Otp Verified", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("myApp", "Something went wrong");
                    }
                });

                requestQueue.add(jsonObjectRequest);
            }
        });


    }
}