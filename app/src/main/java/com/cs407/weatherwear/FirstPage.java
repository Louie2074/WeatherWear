package com.cs407.weatherwear;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FirstPage extends AppCompatActivity {

    // weather URL to get JSON
    private String weatherUrl;
    // API id for URL
    private String apiId = "030314b750cc43e7b39e503dfe37150c";

    private TextView textView;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        // Link the TextView in which the temperature will be displayed
        textView = findViewById(R.id.textView);

        // Create an instance of the Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // On clicking this button, the function to get the coordinates will be called
        findViewById(R.id.btVar1).setOnClickListener(view -> {
            // Function to find the coordinates of the last location
            obtainLocation();
        });
    }

    @SuppressLint("MissingPermission")
    private void obtainLocation() {
        // Get the last location
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Get the latitude and longitude and create the HTTP URL
                    if (location != null) {
                        weatherUrl = "https://api.weatherbit.io/v2.0/current?" +
                                "lat=" + location.getLatitude() +
                                "&lon=" + location.getLongitude() +
                                "&key=" + apiId;
                        // This function will fetch data from URL
                        getTemp();
                    } else {
                        Log.e("lat", "Location is null");
                    }
                });
    }

    private void getTemp() {
        // Instantiate the RequestQueue.
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        String url = weatherUrl;

        // Request a string response from the provided URL.
        StringRequest stringReq = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        // Get the JSON object
                        JSONObject obj = new JSONObject(response);

                        // Get the Array from obj of name - "data"
                        JSONArray arr = obj.getJSONArray("data");

                        // Get the JSON object from the array at index position 0
                        JSONObject obj2 = arr.getJSONObject(0);

                        // Set the temperature and the city name using getString() function
                        textView.setText(obj2.getString("temp") + " deg Celsius in " + obj2.getString("city_name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                // In case of any error
                error -> textView.setText("That didn't work!"));
        queue.add(stringReq);
    }
}
