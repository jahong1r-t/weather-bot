package uz.weather.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import uz.weather.entity.*;
import uz.weather.entity.enums.Language;
import uz.weather.service.ApiService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static uz.weather.db.Datasource.keyboard;
import static uz.weather.entity.enums.Language.ENGLISH;
import static uz.weather.entity.enums.Language.UZBEK;

public interface Message {
    String choseLangUz = "Salom! \uD83C\uDF1F Ob-havo botiga xush kelibsiz. Ro‘yxatdan o‘tmagan bo‘lsangiz, avval tilni tanlang:";

    static String mainMenuMsg(Language lang) {
        return lang == UZBEK ? "Assalomu alaykum. Kerakkli bo'limni tanlng 👇" :
                lang == ENGLISH ? "Hello! \uD83C\uDF1F Pick the section you need \uD83D\uDC47" :
                        "Привет! \uD83C\uDF1F Выберите нужный раздел \uD83D\uDC47";
    }

    static String authSendLocation(Language lang) {
        return lang == UZBEK ? "Ob-havo ma’lumotini bilish uchun shahar nomini yozing yoki lokatsiyangizni yuboring! \uD83D\uDCCD" :
                lang == ENGLISH ? "To get the weather information, enter the city name or send your location! \uD83D\uDCCD" :
                        "Чтобы узнать погоду, введите название города или отправьте свою локацию! \uD83D\uDCCD";
    }

    static String searchMsg(Language lang) {
        return lang == UZBEK ? "Ob-havo ma’lumotini bilmoqchi bo'lgan shahar nomini kiriting! 🔍" :
                lang == ENGLISH ? "Enter the city name to get the weather information! 🔍" :
                        "Введите название города, чтобы узнать погоду!! 🔍";
    }

    static ReplyKeyboard mainPanel(Language lang) {
        return lang == UZBEK ? keyboard(Util.mainMenuUz) :
                lang == ENGLISH ? keyboard(Util.mainMenuEn) :
                        keyboard(Util.mainMenuRu);
    }

    static String getDailyMessage(User user) {
        Response response;
        Language lang = user.getLanguage();

        if (user.getLocation() != null) {
            response = ApiService.getDailyInformation(user.getLocation().getLongitude(), user.getLocation().getLatitude(), null);
        } else {
            response = ApiService.getDailyInformation(null, null, user.getCity());
        }

        if (response.getCod() == 200) {
            Main main = Objects.requireNonNull(response).getMain();
            Clouds clouds = response.getClouds();
            Wind wind = response.getWind();
            Sys sys = response.getSys();
            Coord coord = response.getCoord();
            List<WeatherItem> weather = response.getWeather();

            String weatherDesc = weather != null && !weather.isEmpty() ?
                    weather.get(0).getDescription() : (lang == UZBEK ?
                    "Ma'lumot yo‘q" : (lang == ENGLISH ?
                    "No data available" : "Нет данных"));

            long sunriseTime = (long) sys.getSunrise() * 1000;
            long sunsetTime = (long) sys.getSunset() * 1000;
            String sunrise = new SimpleDateFormat("HH:mm").format(new Date(sunriseTime));
            String sunset = new SimpleDateFormat("HH:mm").format(new Date(sunsetTime));

            return (lang == UZBEK) ? """
                    🌤️ %s uchun ob-havo 🌤️
                    
                    ☁️ Havo holati: %s
                    🌡️ Harorat: %s°C
                    💧 Namlik: %d%%
                    ⏲ Bosim: %d mb
                    🌫️ Ko‘rish masofasi: %d m
                    💨 Shamol: %s m/s, %d°
                    ☁️ Bulut qoplami: %d%%
                    
                    🌅 Quyosh chiqishi: %s
                    🌇 Quyosh botishi: %s
                    🌍 Koordinatalar: %s, %s
                    """.formatted(response.getName(), weatherDesc, main.getTempMin().toString(), main.getHumidity(), main.getPressure(), response.getVisibility(), wind.getSpeed().toString(), wind.getDeg(), clouds.getAll(), sunrise, sunset, coord.getLat().toString(), coord.getLon().toString()) :

                    (lang == ENGLISH) ? """
                            🌤️ Weather in %s 🌤️
                            
                            ☁️ Condition: %s
                            🌡️ Temperature: %s°C
                            💧 Humidity: %d%%
                            ⏲ Pressure: %d mb
                            🌫️ Visibility: %d m
                            💨 Wind: %s m/s, %d°
                            ☁️ Cloud coverage: %d%%
                            
                            🌅 Sunrise: %s
                            🌇 Sunset: %s
                            🌍 Coordinates: %s, %s
                            """.formatted(response.getName(), weatherDesc, main.getTempMin().toString(), main.getHumidity(), main.getPressure(), response.getVisibility(), wind.getSpeed().toString(), wind.getDeg(), clouds.getAll(), sunrise, sunset, coord.getLat().toString(), coord.getLon().toString()) :

                            """
                                    🌤️ Погода в %s 🌤️
                                    
                                    ☁️ Состояние: %s
                                    🌡️ Температура: %s°C
                                    💧 Влажность: %d%%
                                    ⏲ Давление: %d мб
                                    🌫️ Видимость: %d м
                                    💨 Ветер: %s м/с, %d°
                                    ☁️ Облачность: %d%%
                                    
                                    🌅 Восход: %s
                                    🌇 Закат: %s
                                    🌍 Координаты: %s, %s
                                    """.formatted(response.getName(), weatherDesc, main.getTempMin().toString(), main.getHumidity(), main.getPressure(), response.getVisibility(), wind.getSpeed().toString(), wind.getDeg(), clouds.getAll(), sunrise, sunset, coord.getLat().toString(), coord.getLon().toString());
        } else {
            return lang == UZBEK ? "❌ Kechirasiz, kiritilgan shahar topilmadi. Iltimos, shahar nomini tekshirib qaytadan kiriting yoki lokatsiyangizni yuboring!" :
                    lang == ENGLISH ? "❌ Sorry, the specified city was not found. Please check the city name and try again or send your location!" :
                            "❌ Извините, указанный город не найден. Пожалуйста, проверьте название города и попробуйте снова или отправьте свою локацию!";
        }
    }

    static String getWeeklyMessage(User user) {
        WeatherForecast forecast;
        Language lang = user.getLanguage();

        if (user.getLocation() != null) {
            forecast = ApiService.getWeeklyInformation(user.getLocation().getLongitude(), user.getLocation().getLatitude(), null);
        } else {
            forecast = ApiService.getWeeklyInformation(null, null, user.getCity());
        }

        if (forecast != null && "200".equals(forecast.getCod())) {
            List<ForecastItem> forecastList = forecast.getList();
            City city = forecast.getCity();

            StringBuilder message = new StringBuilder();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            message.append((lang == Language.UZBEK) ?
                    String.format("🌤️ %s uchun 5 kunlik ob-havo prognozi 🌤️\n\n", city.getName()) :
                    (lang == Language.ENGLISH) ?
                            String.format("🌤️ 5-day weather forecast for %s 🌤️\n\n", city.getName()) :
                            String.format("🌤️ Прогноз погоды на 5 дней для %s 🌤️\n\n", city.getName()));

            String lastDate = "";
            for (ForecastItem item : forecastList) {
                String currentDate = dateFormat.format(new Date(item.getDt() * 1000));
                if (!currentDate.equals(lastDate)) {
                    lastDate = currentDate;

                    Main main = item.getMain();
                    List<WeatherItem> weather = item.getWeather();
                    Wind wind = item.getWind();
                    Clouds clouds = item.getClouds();

                    String weatherDesc = weather != null && !weather.isEmpty() ?
                            weather.get(0).getDescription() : (lang == Language.UZBEK ?
                            "Ma'lumot yo‘q" : (lang == Language.ENGLISH ?
                            "No data available" : "Нет данных"));

                    double temp = (main.getTemp() != null) ? main.getTemp() : 0.0;
                    double speed = (wind.getSpeed() != null) ? (double) wind.getSpeed() : 0.0;

                    message.append((lang == Language.UZBEK) ?
                            String.format(
                                    """
                                            📅 %s:
                                            ☁️ Havo holati: %s
                                            🌡️ Harorat: %.1f°C
                                            💧 Namlik: %d%%
                                            💨 Shamol: %.1f m/s
                                            ☁️ Bulut qoplami: %d%%
                                            
                                            
                                            """,
                                    currentDate, weatherDesc, temp, main.getHumidity(), speed, clouds.getAll()
                            ) :
                            (lang == Language.ENGLISH) ?
                                    String.format(
                                            """
                                                    📅 %s:
                                                    ☁️ Condition: %s
                                                    🌡️ Temperature: %.1f°C
                                                    💧 Humidity: %d%%
                                                    💨 Wind: %.1f m/s
                                                    ☁️ Cloud coverage: %d%%
                                                    
                                                    
                                                    """,
                                            currentDate, weatherDesc, temp, main.getHumidity(), speed, clouds.getAll()
                                    ) :
                                    String.format(
                                            """
                                                    📅 %s:
                                                    ☁️ Состояние: %s
                                                    🌡️ Температура: %.1f°C
                                                    💧 Влажность: %d%%
                                                    💨 Ветер: %.1f м/с
                                                    ☁️ Облачность: %d%%
                                                    
                                                    
                                                    """,
                                            currentDate, weatherDesc, temp, main.getHumidity(), speed, clouds.getAll()
                                    ));
                }
            }

            long sunriseTime = city.getSunrise() * 1000L; // Casting o‘rniga to‘g‘ridan-to‘g‘ri ko‘paytma
            long sunsetTime = city.getSunset() * 1000L;
            String sunrise = timeFormat.format(new Date(sunriseTime));
            String sunset = timeFormat.format(new Date(sunsetTime));

            double lat = (city.getCoord().getLat() != null) ? (double) city.getCoord().getLat() : 0.0;
            double lon = (city.getCoord().getLon() != null) ? (double) city.getCoord().getLon() : 0.0;

            message.append((lang == Language.UZBEK) ?
                    String.format("🌅 Quyosh chiqishi: %s\n🌇 Quyosh botishi: %s\n🌍 Koordinatalar: %.2f, %.2f",
                            sunrise, sunset, lat, lon) :
                    (lang == Language.ENGLISH) ?
                            String.format("🌅 Sunrise: %s\n🌇 Sunset: %s\n🌍 Coordinates: %.2f, %.2f",
                                    sunrise, sunset, lat, lon) :
                            String.format("🌅 Восход: %s\n🌇 Закат: %s\n🌍 Координаты: %.2f, %.2f",
                                    sunrise, sunset, lat, lon));

            return message.toString();
        } else {
            return lang == Language.UZBEK ?
                    "❌ Kechirasiz, kiritilgan shahar topilmadi. Iltimos, shahar nomini tekshirib qaytadan kiriting yoki lokatsiyangizni yuboring!" :
                    lang == Language.ENGLISH ?
                            "❌ Sorry, the specified city was not found. Please check the city name and try again or send your location!" :
                            "❌ Извините, указанный город не найден. Пожалуйста, проверьте название города и попробуйте снова или отправьте свою локацию!";
        }
    }
}

