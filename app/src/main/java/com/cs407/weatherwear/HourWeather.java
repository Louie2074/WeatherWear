package com.cs407.weatherwear;

public class HourWeather {
    private long hourEpoch;
    private double temp;
    private String condition;
    private double windMph;
    private double humidity;
    private double chanceRain;
    private double chanceSnow;
    private double uv;

    // Constructor
    public HourWeather(long hourEpoch, double temp, String condition, double windMph, double humidity, double chanceRain, double chanceSnow, double uv) {
        this.hourEpoch = hourEpoch;
        this.temp = temp;
        this.condition = condition;
        this.windMph = windMph;
        this.humidity = humidity;
        this.chanceRain = chanceRain;
        this.chanceSnow = chanceSnow;
        this.uv = uv;
    }
    public long getHourEpoch() {
        return hourEpoch;
    }

    public double getTemp() {
        return temp;
    }

    public String getCondition() {
        return condition;
    }

    public double getWindMph() {
        return windMph;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getChanceRain() {
        return chanceRain;
    }

    public double getChanceSnow() {
        return chanceSnow;
    }

    public double getUv() {
        return uv;
    }
    @Override
    public String toString() {
        return "HourWeather{" +
                "hourEpoch=" + hourEpoch +
                ", temp=" + temp +
                ", condition='" + condition + '\'' +
                ", windMph=" + windMph +
                ", humidity=" + humidity +
                ", chanceRain=" + chanceRain +
                ", chanceSnow=" + chanceSnow +
                ", uv=" + uv +
                '}';
    }

}
