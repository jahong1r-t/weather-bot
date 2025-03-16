package uz.weather.util;

import uz.weather.entity.Response;

public interface Util {
    String[][] mainMenu = {{Button.weather_now_uz}};

    static String getWeatherMessage(Response response) {
        return "";
    }
}
