package com.cs407.weatherwear;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FirstPage extends AppCompatActivity {
    private TextView textView;
    private FusedLocationProviderClient fusedLocationClient;

    private WeatherAPITools tools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        SharedPreferences sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
        String jsonString = sharedPreferences.getString("json_data", null);
        try {
            tools = new WeatherAPITools(new JSONObject(jsonString));
            TextView today = (TextView) findViewById(R.id.currentDay);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
