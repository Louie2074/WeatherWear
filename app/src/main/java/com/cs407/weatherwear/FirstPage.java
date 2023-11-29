package com.cs407.weatherwear;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FirstPage extends AppCompatActivity {
    private TextView locationText, tempText, weatherCondText, highAndLowText, hourlyForecastText;
    private ImageView weatherCondIcon;

    private WeatherAPITools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        // Initialize TextViews
        locationText = findViewById(R.id.locationText);
        tempText = findViewById(R.id.tempText);
        weatherCondText = findViewById(R.id.weatherCondText);
        weatherCondIcon = findViewById(R.id.weatherCondIcon);
        highAndLowText = findViewById(R.id.highAndLowText);
        hourlyForecastText = findViewById(R.id.hourlyForecastTextList);

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
    private void updateUIWithWeatherData() {
        try {
            // Update TextViews with weather information
            String[] weatherDetailsToday = tools.getCurrentWeather();
            String[] locationDetailsToday = tools.getLocationDetails();
            String[] minMaxTempsToday = tools.getMinMaxTemp();
            locationText.setText(locationDetailsToday[0]);
            tempText.setText(weatherDetailsToday[0] + "°F");
            weatherCondText.setText(weatherDetailsToday[1]);
            int iconId = getResources().getIdentifier(weatherDetailsToday[2].substring(6, 7)+weatherDetailsToday[2].substring(12, 15), "drawable", getPackageName());
            weatherCondIcon.setImageResource(iconId);
            highAndLowText.setText("High: "+minMaxTempsToday[1]+"°F Low: "+minMaxTempsToday[0]+"°F");

            ArrayList<HourWeather> hourlyForecastDetails = tools.getHourlyForecast();
            SimpleDateFormat sdf = new SimpleDateFormat("h a", Locale.getDefault());
            for (int i = 0; i < hourlyForecastDetails.size() - 1; i++) {
                // .size() - 1 because hourlyForecast gets up to 1 AM
                HourWeather curr = hourlyForecastDetails.get(i);
                long epochMs = curr.getHourEpoch() * 1000;
                hourlyForecastText.append(sdf.format(new Date(epochMs))+": "+curr.getTemp()+"°F");
                // need to fix text wrapping. the end of one line can get cut off.
                if (i < hourlyForecastDetails.size() - 2) {
                    hourlyForecastText.append("\n");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
