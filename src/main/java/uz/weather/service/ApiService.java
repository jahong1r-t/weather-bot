package uz.weather.service;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import uz.weather.entity.Response;
import uz.weather.entity.WeatherForecast;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiService {

    static HttpClient httpClient = HttpClient.newHttpClient();

    static Gson gson = new Gson();

    @SneakyThrows
    public static Response getDailyInformation(Double lon, Double lat, String city) {
        String uri = null;

        if (lon != null && lat != null) {
            uri = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=fc81fcf9780060667be7fa3643713420&units=metric";
        } else {
            uri = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=fc81fcf9780060667be7fa3643713420&units=metric";
        }

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(uri))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.err.println(response.statusCode());
        System.err.println(uri);


        return gson.fromJson(response.body(), Response.class);
    }

    @SneakyThrows
    public static WeatherForecast getWeeklyInformation(Double lon, Double lat, String city) {
        String uri = null;

        if (lon != null && lat != null) {
            uri = "https://api.openweathermap.org/data/2.5/forecast?lat=" + lat + "&lon=" + lon + "&appid=fc81fcf9780060667be7fa3643713420&units=metric";
        } else {
            uri = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=fc81fcf9780060667be7fa3643713420&units=metric";
        }


        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI(uri))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.err.println(response.statusCode());
        System.err.println(uri);
        System.out.println(response.body());

        return gson.fromJson(response.body(), WeatherForecast.class);

    }
}
