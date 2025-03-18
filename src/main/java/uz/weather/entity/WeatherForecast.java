package uz.weather.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class WeatherForecast {
    @SerializedName("cod")
    private String cod;

    @SerializedName("message")
    private String message;

    @SerializedName("cnt")
    private int cnt;

    @SerializedName("list")
    private List<ForecastItem> list;

    @SerializedName("city")
    private City city;
}