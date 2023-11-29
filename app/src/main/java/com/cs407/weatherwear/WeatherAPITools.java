package com.cs407.weatherwear;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class WeatherAPITools {
    private JSONObject json;
    public WeatherAPITools(JSONObject JSON){
        this.json = JSON;
    }
    public String[] getLocationDetails() throws JSONException {
        JSONObject obj = (JSONObject) json.get("location");
        return new String[]{obj.get("name").toString(),
                obj.get("region").toString(), obj.get("country").toString(),
                obj.get("lat").toString(), obj.get("lon").toString(), obj.get("localtime").toString()};
    }
    public String[] getCurrentWeather() throws JSONException {
        JSONObject obj = (JSONObject) json.get("current");
        return new String[]{obj.get("temp_f").toString(),((JSONObject) obj.get("condition")).get("text").toString(),((JSONObject) obj.get("condition")).get("icon").toString().substring(29)};
    }

    public String[] getMinMaxTemp() throws JSONException {
        JSONObject obj = (JSONObject) json.get("forecast");
        JSONArray forecastArray = obj.getJSONArray("forecastday");
        JSONObject todaysWeather = (JSONObject)forecastArray.get(0);
        JSONObject day = (JSONObject)todaysWeather.get("day");
        return new String[]{day.get("mintemp_f").toString(), day.get("maxtemp_f").toString()};
    }
    public ArrayList<HourWeather> getHourlyForecast() throws JSONException {
        long currentEpoch = System.currentTimeMillis()/1000;
        JSONObject obj = (JSONObject) json.get("forecast");
        JSONArray forecastArray = obj.getJSONArray("forecastday");
        JSONObject todaysWeather = (JSONObject)forecastArray.get(0);
        JSONArray hour = todaysWeather.getJSONArray("hour");
        ArrayList<HourWeather> weatherArray = new ArrayList<>();
        for(int i = 0; i<hour.length(); i++){
            JSONObject currentHour = (JSONObject)hour.get(i);
            if(Long.parseLong(currentHour.get("time_epoch").toString())<currentEpoch){
                continue;
            }

            long hourEpoch = Long.parseLong(currentHour.get("time_epoch").toString());
            double temp = Double.parseDouble(currentHour.get("temp_f").toString());
            String conditionText = ((JSONObject) currentHour.get("condition")).get("text").toString();
            double windMph = Double.parseDouble(currentHour.get("wind_mph").toString());
            double humidity = Double.parseDouble(currentHour.get("humidity").toString());
            double chanceOfRain = Double.parseDouble(currentHour.get("chance_of_rain").toString());
            double chanceOfSnow = Double.parseDouble(currentHour.get("chance_of_snow").toString());
            double uvIndex = Double.parseDouble(currentHour.get("uv").toString());

            HourWeather hourObj = new HourWeather(hourEpoch, temp, conditionText, windMph, humidity, chanceOfRain, chanceOfSnow, uvIndex);

            weatherArray.add(hourObj);
        }

        return weatherArray;
    }


}
