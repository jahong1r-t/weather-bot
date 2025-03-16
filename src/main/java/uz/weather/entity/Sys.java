package uz.weather.entity;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Sys{

	@SerializedName("country")
	private String country;

	@SerializedName("sunrise")
	private int sunrise;

	@SerializedName("sunset")
	private int sunset;

	@SerializedName("id")
	private int id;

	@SerializedName("type")
	private int type;
}