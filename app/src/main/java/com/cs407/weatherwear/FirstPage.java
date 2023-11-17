package com.cs407.weatherwear;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class FirstPage extends AppCompatActivity {
    private TextView todayText, minTempTextView, maxTempTextView;


    private WeatherAPITools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        // Initialize TextViews
        todayText = findViewById(R.id.todayText);
        minTempTextView = findViewById(R.id.minTempTextView);
        maxTempTextView = findViewById(R.id.maxTempTextView);

        SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        String jsonString = sharedPreferences.getString("json_data", null);

        if (jsonString != null) {
            try {
                tools = new WeatherAPITools(new JSONObject(jsonString));
                // Use existing data if available
                updateUIWithWeatherData();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Fetch weather data using Volley
            fetchWeatherData();
        }
    }

    // Method to fetch weather data using Volley
    private void fetchWeatherData() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.weatherapi.com/v1/forecast.json?key=YOUR_API_KEY"; // Replace with your API key

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        tools = new WeatherAPITools(response);
                        // Update UI with weather data
                        updateUIWithWeatherData();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle API call failures here
                Toast.makeText(FirstPage.this, "Weather data retrieval failed!", Toast.LENGTH_SHORT).show();
                Log.e("WeatherAPI", "Error: " + error.toString());
            }
        });

        queue.add(jsonObjectRequest);
    }

    // Update UI with weather data
    // Update UI with weather data
    private void updateUIWithWeatherData() {
        try {
            // Update TextViews with weather information
            String[] weatherDetailsToday = tools.getCurrentWeather();
            todayText.setText("Today's Weather: " + weatherDetailsToday[0] + "°F, " + weatherDetailsToday[1]);

            // Fetch min and max temperature
            String[] minMaxTemp = tools.getMinMaxTemp();
            String minTemp = minMaxTemp[0];
            String maxTemp = minMaxTemp[1];

            // Display min and max temperature in TextViews
            // For example, if you have separate TextViews for min and max temperature:
            minTempTextView.setText("Today's Low: " + minTemp + "°F");
            maxTempTextView.setText("Today's High: " + maxTemp + "°F");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
