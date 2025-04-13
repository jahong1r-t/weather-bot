package uz.weather;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uz.weather.bot.MainBot;
import uz.weather.service.DailyWeatherService;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(new MainBot());
        new DailyWeatherService().startDailyWeatherService();
    }
}
