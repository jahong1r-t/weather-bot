package uz.weather.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import uz.weather.bot.MainBot;
import uz.weather.entity.User;
import uz.weather.entity.enums.Language;
import uz.weather.entity.enums.State;
import uz.weather.util.Button;
import uz.weather.util.Message;

import static uz.weather.db.Datasource.states;
import static uz.weather.db.Datasource.users;
import static uz.weather.util.Button.*;
import static uz.weather.util.Message.*;

public class UserService extends MainBot {
    public void service(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        states.putIfAbsent(chatId, State.MAIN_MENU);
        Language lang = users.get(chatId).getLanguage();

        State currentState = states.get(chatId);

        if (currentState == State.MAIN_MENU) {
            switch (text) {
                case "/start" -> sendMessage(chatId, Message.mainMenuMsg(lang), mainPanel(lang));
                case today_uz, today_en, today_ru ->
                        sendMessage(chatId, getDailyMessage(users.get(chatId)), "src/main/resources/weather.jpg");
                case week_en, week_ru, week_uz ->
                        sendMessage(chatId, getWeeklyMessage(users.get(chatId)), "src/main/resources/weather.jpg");
                case search_en, search_ru, search_uz -> {
                    sendMessage(chatId, searchMsg(lang));
                    states.put(chatId, State.SEARCH_LOCATION);
                }
            }
        } else if (currentState == State.SEARCH_LOCATION) {
            sendMessage(chatId, getDailyMessage(new User(null, null, null, null, text, lang)));
            states.remove(chatId);
        }
    }
}
