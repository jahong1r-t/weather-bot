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
    String choseLangUz = "Salom! \uD83C\uDF1F Ob-havo botiga xush kelibsiz.\nO'zingizga kerakli tilni tanlang tilni tanlang:";
    String errMsgUz = "Salom! \uD83C\uDF1F Ob-havo botiga xush kelibsiz.\nO'zingizga kerakli tilni tanlang tilni tanlang:";

    static String changeLangMsg(Language lang) {
        return lang == UZBEK ? "O'zingizga kerakli tilni tanlang tilni tanlang:" :
                lang == ENGLISH ? "Select the language you want:" :
                        "Выберите нужный вам язык:";
    }

    static String mainMenuMsg(Language lang) {
        return lang == UZBEK ? "Assalomu alaykum. Kerakkli bo'limni tanlng 👇" :
                lang == ENGLISH ? "Hello! \uD83C\uDF1F Pick the section you need \uD83D\uDC47" :
                        "Привет! \uD83C\uDF1F Выберите нужный раздел \uD83D\uDC47";
    }

    static String settingsMenuMsg(Language lang) {
        return lang == UZBEK ? "Sozlamalar" :
                lang == ENGLISH ? "Settings" :
                        "Настройки";
    }

    static String authSendLocation(Language lang) {
        return lang == UZBEK ? "Ob-havo ma’lumotini bilish uchun shahar nomini yozing yoki lokatsiyangizni yuboring!" :
                lang == ENGLISH ? "To get the weather information, enter the city name or send your location!" :
                        "Чтобы узнать погоду, введите название города или отправьте свою локацию!";
    }

    static String unSubscribedMsg(Language lang) {
        return lang == UZBEK ? "Har kunlik ob-havo xabarlari muvaffaqiyatli o‘chirildi. Endi ob-havo xabarlari olmaysiz." :
                lang == ENGLISH ? "Daily weather updates have been successfully disabled. You will no longer receive weather notifications." :
                        "Ежедневные обновления погоды успешно отключены. Вы больше не будете получать уведомления о погоде.";
    }

    static String subscribedMsg(Language lang) {
        return lang == UZBEK ? "Har kunlik ob-havo xabarlari muvaffaqiyatli yoqildi! Endi har kuni soat 07:00 da ob-havo ma'lumotlarini olasiz." :
                lang == ENGLISH ? "Daily weather updates have been successfully enabled! You will now receive weather information every day at 07:00." :
                        "Ежедневные обновления погоды успешно включены! Теперь вы будете получать информацию о погоде каждый день в 07:00.";
    }


    static String searchMsg(Language lang) {
        return lang == UZBEK ? "Ob-havo ma’lumotini bilmoqchi bo'lgan shahar nomini kiriting! 🔍" :
                lang == ENGLISH ? "Enter the city name to get the weather information! 🔍" :
                        "Введите название города, чтобы узнать погоду!! 🔍";
    }

    static String change_lang_success_msg(Language lang) {
        return lang == UZBEK ? "✅ Til muvaffaqiyatli o'zgartirildi" :
                lang == ENGLISH ? "✅ Language successfully changed" :
                        "✅ Язык успешно изменен";
    }

    static String changeLocationSuccessMsg(Language lang) {
        return lang == UZBEK ? "Joylashuv muvaffaqiyatli o‘zgartirildi" :
                lang == ENGLISH ? "Location successfully changed"
                        : "Местоположение успешно изменено";
    }

    static ReplyKeyboard mainPanel(Language lang) {
        return lang == UZBEK ? keyboard(Util.mainMenuUz) :
                lang == ENGLISH ? keyboard(Util.mainMenuEn) :
                        keyboard(Util.mainMenuRu);
    }

    static ReplyKeyboard settingsPanel(Language lang) {
        return lang == UZBEK ? keyboard(Util.settingsPanelUz) :
                lang == ENGLISH ? keyboard(Util.settingsPanelEn) :
                        keyboard(Util.settingsPanelRu);
    }

    static DailyWeatherInfo getDailyMessage(User user) {
        Response response;
        Language lang = user.getLanguage();

        if (user.getLocation() != null) {
            response = ApiService.getDailyInformation(user.getLocation().getLongitude(), user.getLocation().getLatitude(), null);
        } else {
            response = ApiService.getDailyInformation(null, null, user.getCity());
        }

        if (Objects.requireNonNull(response).getCod() == 200) {
            user.setCityFound(true);

            Main main = Objects.requireNonNull(response).getMain();
            Clouds clouds = response.getClouds();
            Wind wind = response.getWind();
            Sys sys = response.getSys();
            Coord coord = response.getCoord();
            List<WeatherItem> weather = response.getWeather();
            Rain rain = response.getRain();

            String weatherDesc = weather != null && !weather.isEmpty() ?
                    weather.get(0).getDescription() : (lang == UZBEK ? "Ma'lumot yo‘q" : (lang == ENGLISH ? "No data available" : "Нет данных"));

            long sunriseTime = (long) sys.getSunrise() * 1000;
            long sunsetTime = (long) sys.getSunset() * 1000;
            String sunrise = new SimpleDateFormat("HH:mm").format(new Date(sunriseTime));
            String sunset = new SimpleDateFormat("HH:mm").format(new Date(sunsetTime));

            String rainInfo = "";
            if (rain != null) {
                double rainLast3Hours = rain.getThreeHour();
                if (rainLast3Hours > 0) {
                    rainInfo = (lang == UZBEK) ?
                            "☔ Yomg‘ir yog‘moqda. Oxirgi 3 soatda: " + rainLast3Hours + " mm" :
                            (lang == ENGLISH) ?
                                    "☔ It's raining. Last 3 hours: " + rainLast3Hours + " mm" :
                                    "☔ Идет дождь. За последние 3 часа: " + rainLast3Hours + " мм";
                }
            }

            String message = (lang == UZBEK) ? """
                    🌤️ %s uchun ob-havo 🌤️
                    
                    ☁️ Havo holati: %s
                    🌡️ Harorat: %s°C
                    💧 Namlik: %d%%
                    ⏲ Bosim: %d mb
                    🌫️ Ko‘rish masofasi: %d m
                    💨 Shamol: %s m/s, %d°
                    ☁️ Bulut qoplami: %d%%
                    %s
                    
                    🌅 Quyosh chiqishi: %s
                    🌇 Quyosh botishi: %s
                    🌍 Koordinatalar: %s, %s
                    """.formatted(response.getName(), weatherDesc, main.getTempMin().toString(), main.getHumidity(), main.getPressure(), response.getVisibility(), wind.getSpeed().toString(), wind.getDeg(), clouds.getAll(), rainInfo, sunrise, sunset, coord.getLat().toString(), coord.getLon().toString()) :

                    (lang == ENGLISH) ? """
                            🌤️ Weather in %s 🌤️
                            
                            ☁️ Condition: %s
                            🌡️ Temperature: %s°C
                            💧 Humidity: %d%%
                            ⏲ Pressure: %d mb
                            🌫️ Visibility: %d m
                            💨 Wind: %s m/s, %d°
                            ☁️ Cloud coverage: %d%%
                            %s
                            
                            🌅 Sunrise: %s
                            🌇 Sunset: %s
                            🌍 Coordinates: %s, %s
                            """.formatted(response.getName(), weatherDesc, main.getTempMin().toString(), main.getHumidity(), main.getPressure(), response.getVisibility(), wind.getSpeed().toString(), wind.getDeg(), clouds.getAll(), rainInfo, sunrise, sunset, coord.getLat().toString(), coord.getLon().toString()) :

                            """
                                    🌤️ Погода в %s 🌤️
                                    
                                    ☁️ Состояние: %s
                                    🌡️ Температура: %s°C
                                    💧 Влажность: %d%%
                                    ⏲ Давление: %d мб
                                    🌫️ Видимость: %d м
                                    💨 Ветер: %s м/с, %d°
                                    ☁️ Облачность: %d%%
                                    %s
                                    
                                    🌅 Восход: %s
                                    🌇 Закат: %s
                                    🌍 Координаты: %s, %s
                                    """.formatted(response.getName(), weatherDesc, main.getTempMin().toString(), main.getHumidity(), main.getPressure(), response.getVisibility(), wind.getSpeed().toString(), wind.getDeg(), clouds.getAll(), rainInfo, sunrise, sunset, coord.getLat().toString(), coord.getLon().toString());

            String imagePath = getWeatherImagePath(response);
            return new DailyWeatherInfo(message, imagePath);
        } else {
            user.setCityFound(false);
            String errorMessage = lang == UZBEK ? "❌ Kechirasiz, kiritilgan shahar topilmadi. Iltimos, shahar nomini tekshirib qaytadan kiriting yoki lokatsiyangizni yuboring!" :
                    lang == ENGLISH ? "❌ Sorry, the specified city was not found. Please check the city name and try again or send your location!" :
                            "❌ Извините, указанный город не найден. Пожалуйста, проверьте название города и попробуйте снова или отправьте свою локацию!";
            return new DailyWeatherInfo(errorMessage, "src/main/resources/weather.jpg");
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

        if (Objects.equals(forecast.getCod(), "200")) {
            user.setCityFound(true);

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

            long sunriseTime = city.getSunrise() * 1000L;
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
            user.setCityFound(false);
            return lang == Language.UZBEK ?
                    "❌ Kechirasiz, kiritilgan shahar topilmadi. Iltimos, shahar nomini tekshirib qaytadan kiriting yoki lokatsiyangizni yuboring!" :
                    lang == Language.ENGLISH ?
                            "❌ Sorry, the specified city was not found. Please check the city name and try again or send your location!" :
                            "❌ Извините, указанный город не найден. Пожалуйста, проверьте название города и попробуйте снова или отправьте свою локацию!";
        }
    }

    static String getWeatherImagePath(Response response) {
        double temp = response.getMain().getTemp();
        if (temp <= 10) {
            return "src/main/resources/cold.jpg";
        } else if (temp <= 25) {
            return "src/main/resources/warm.jpg";
        } else {
            return "src/main/resources/hot.jpg";
        }
    }

    record DailyWeatherInfo(String message, String imagePath) {
    }
}


