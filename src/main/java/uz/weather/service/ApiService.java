package uz.weather.service;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import uz.weather.entity.Response;
import uz.weather.entity.WeatherForecast;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class ApiService {
    static HttpClient httpClient = HttpClient.newHttpClient();
    static Gson gson = new Gson();

    @SneakyThrows
    public static Response getDailyInformation(Double lon, Double lat, String city) {
        log.info("New request for daily weather.");

        String uri;
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

        if (response.statusCode() == 200) {
            log.info("Request completed. Status code => {}\nURL => {}", response.statusCode(), uri);
        } else {
            log.warn("Failed to fetch daily weather data. Status code: {}", response.statusCode());
        }
        return gson.fromJson(response.body(), Response.class);
    }

    @SneakyThrows
    public static WeatherForecast getWeeklyInformation(Double lon, Double lat, String city) {
        log.info("New request for weekly weather.");

        String uri;
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

        if (response.statusCode() == 200) {
            log.info("Request completed. Status code => {}\nURI => {}", response.statusCode(), uri);
        } else {
            log.warn("Failed to fetch weekly weather data. Status code: {}", response.statusCode());
        }

        return gson.fromJson(response.body(), WeatherForecast.class);

    }
}