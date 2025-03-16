package uz.weather.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class City {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("coord")
    private Coord coord;

    @SerializedName("country")
    private String country;

    @SerializedName("population")
    private int population;

    @SerializedName("timezone")
    private int timezone;

    @SerializedName("sunrise")
    private long sunrise;

    @SerializedName("sunset")
    private long sunset;
}