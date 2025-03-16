package uz.weather.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Coord{

	@SerializedName("lon")
	private Object lon;

	@SerializedName("lat")
	private Object lat;
}