	package uz.weather.entity;

	import com.google.gson.annotations.SerializedName;
	import lombok.Getter;

	@Getter
	public class WeatherItem{

		@SerializedName("icon")
		private String icon;

		@SerializedName("description")
		private String description;

		@SerializedName("main")
		private String main;

		@SerializedName("id")
		private int id;
	}