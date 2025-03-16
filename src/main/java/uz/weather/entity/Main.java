package uz.weather.entity;

import com.google.gson.annotations.SerializedName;

public class Main{

	@SerializedName("temp")
	private Object temp;

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