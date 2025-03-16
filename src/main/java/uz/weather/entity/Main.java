package uz.weather.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Main {

    @SerializedName("temp")
    private Double temp;

    @SerializedName("temp_min")
    private Object tempMin;

    @SerializedName("humidity")
    private int humidity;

    @SerializedName("pressure")
    private int pressure;

    @SerializedName("feels_like")
    private Object feelsLike;

    @SerializedName("temp_max")
    private Object tempMax;
}