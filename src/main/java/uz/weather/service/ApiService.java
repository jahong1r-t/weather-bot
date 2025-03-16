package uz.weather.service;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import uz.weather.entity.Response;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiService {

    static HttpClient httpClient = HttpClient.newHttpClient();

    static Gson gson = new Gson();

    @SneakyThrows
    public static Response getInformation(Double lon, Double lat, String date) {

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(new URI("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=fc81fcf9780060667be7fa3643713420&units=metric&lang=uz"))
                .GET()
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=fc81fcf9780060667be7fa3643713420&units=metric&lang=uz");

        System.err.println(response.statusCode());


        return gson.fromJson(response.body(), Response.class);
    }
}
