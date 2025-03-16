package uz.weather.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import java.util.List;

@Getter
public class ForecastItem {
    @SerializedName("dt")
    private long dt;

    @SerializedName("main")
    private Main main;

    @SerializedName("weather")
    private List<WeatherItem> weather;

    @SerializedName("clouds")
    private Clouds clouds;

    @SerializedName("wind")
    private Wind wind;

    @SerializedName("visibility")
    private int visibility;

    @SerializedName("pop")
    private double pop;

    @SerializedName("rain")
    private Rain rain;

    @SerializedName("sys")
    private Sys sys;

    @SerializedName("dt_txt")
    private String dtTxt;
}