package com.cs407.weatherwear;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WeatherPage extends AppCompatActivity {
    private TextView todayText, hourlyForecastText;

    private WeatherAPITools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);

        todayText = findViewById(R.id.todayText);
        hourlyForecastText = findViewById(R.id.hourlyForecastText);

        SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        String jsonString = sharedPreferences.getString("json_data", null);
        Button settings = findViewById(R.id.settingsButton);
        Button outfitButton1 = findViewById(R.id.recommButton1);
        Button outfitButton2 = findViewById(R.id.recommButton2);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherPage.this, Settings.class);
                startActivity(intent);
            }
        });

        outfitButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherPage.this, OutfitRecommendation.class);
                startActivity(intent);
            }
        });
        outfitButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeatherPage.this, OutfitRecommendation.class);
                startActivity(intent);
            }
        });

        if (jsonString != null) {
            try {
                tools = new WeatherAPITools(new JSONObject(jsonString));
                updateUIWithWeatherData();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }


    // Update UI with weather data
    private void updateUIWithWeatherData() {
        try {
            // Update TextViews with weather information
            String[] weatherDetailsToday = tools.getCurrentWeather();
            String[] locationDetailsToday = tools.getLocationDetails();
            String[] minMaxTempsToday = tools.getMinMaxTemp();
            todayText.setText(locationDetailsToday[0]);
            todayText.append("\n"+weatherDetailsToday[0] + "°F");
            todayText.append("\n"+weatherDetailsToday[1]);
            todayText.append("\nHigh: "+minMaxTempsToday[1]+"°F Low: "+minMaxTempsToday[0]+"°F");

            ArrayList<HourWeather> hourlyForecastDetails = tools.getHourlyForecast();
            SimpleDateFormat sdf = new SimpleDateFormat("h a", Locale.getDefault());
            for (int i = 0; i < hourlyForecastDetails.size(); i++) {
                HourWeather curr = hourlyForecastDetails.get(i);
                long epochMs = curr.getHourEpoch() * 1000;
                hourlyForecastText.append(sdf.format(new Date(epochMs))+": "+curr.getTemp()+"°F");
                // need to fix text wrapping. the end of one line can get cut off.
                if (i < hourlyForecastDetails.size() - 1) {
                    hourlyForecastText.append("\n");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
