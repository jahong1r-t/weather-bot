package uz.weather.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Clouds{
	@SerializedName("all")
	private int all;
}