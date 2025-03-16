package uz.weather.entity;

import com.google.gson.annotations.SerializedName;

public class Coord{

	@SerializedName("lon")
	private Object lon;

	@SerializedName("lat")
	private Object lat;
}