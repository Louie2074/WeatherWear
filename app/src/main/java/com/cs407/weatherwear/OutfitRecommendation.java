package com.cs407.weatherwear;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OutfitRecommendation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_recommendation);

        Button weatherButton = findViewById(R.id.weatherButton);
        Button settingsButton = findViewById(R.id.settingsButton);

        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OutfitRecommendation.this, WeatherPage.class);
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OutfitRecommendation.this, Settings.class);
                startActivity(intent);
            }
        });
    }
}