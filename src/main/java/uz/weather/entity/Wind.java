package uz.weather.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Wind {
    @SerializedName("deg")
    private int deg;

    @SerializedName("speed")
    private Object speed;
}