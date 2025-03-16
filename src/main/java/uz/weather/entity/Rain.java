package uz.weather.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Rain {
    @SerializedName("3h")
    private double threeHour;
}