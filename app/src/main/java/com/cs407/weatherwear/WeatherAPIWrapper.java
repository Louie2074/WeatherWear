package com.cs407.weatherwear;
import android.os.Handler;
import android.os.Looper;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherAPIWrapper {
    private double latitude;
    private double longitude;
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    public WeatherAPIWrapper(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double newLatitude) {
        this.latitude = newLatitude;
    }
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double newLongitude) {
        this.longitude = newLongitude;
    }
    private String getURL(){
        String apikey = "47eeeb8c91c44b9a8d4162810230411";
        return "https://api.weatherapi.com/v1/forecast.json?key="+ apikey +"&q="+this.latitude+","+this.longitude+"&days=1";
    }

    public interface Callback {
        void onResult(JSONObject result);
        void onError(Exception e);
    }
    public void getWeatherData(final Callback callback) {
        new Thread(new Runnable() {
            HttpURLConnection connection = null;
            @Override
            public void run() {
                try {
                    URL url = new URL(getURL());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream inputStream = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        mainThreadHandler.post(() -> {
                            if (callback != null) {
                                callback.onResult(jsonResponse);
                            }
                        });
                    } else {
                        throw new IOException("Unexpected HTTP response: " + responseCode);
                    }
                } catch (final Exception e) {
                    e.printStackTrace();
                    mainThreadHandler.post(() -> {
                        if (callback != null) {
                            callback.onError(e);
                        }
                    });
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
