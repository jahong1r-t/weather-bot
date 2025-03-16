package uz.weather.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import uz.weather.bot.MainBot;
import uz.weather.entity.User;
import uz.weather.entity.enums.Language;
import uz.weather.entity.enums.State;
import uz.weather.util.Message;
import uz.weather.util.Util;


import static uz.weather.db.Datasource.*;
import static uz.weather.util.Button.*;
import static uz.weather.util.Message.mainPanel;

public class AuthService extends MainBot {
    public void service(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        var from = update.getMessage().getFrom();

        if (isHasUser(chatId)) {
            new UserService().service(update);
        } else {

            State currentState = states.getOrDefault(chatId, State.CHOOSE_LANGUAGE);

            if (currentState == State.CHOOSE_LANGUAGE) {
                switch (text) {
                    case "/start" -> sendMessage(chatId, Message.choseLangUz, keyboard(Util.languageMenu));
                    case UZ, EN, RU -> {
                        User build = User.builder()
                                .chatId(chatId)
                                .userName(from.getUserName() != null ? from.getUserName() : "Unknown")
                                .fullName(from.getLastName() != null ? from.getFirstName() + " " + from.getLastName() : from.getFirstName())
                                .language(text.equals(UZ) ? Language.UZBEK : text.equals(EN) ? Language.ENGLISH : Language.RUSSIAN)
                                .build();
                        users.put(chatId, build);

                        sendMessage(chatId, Message.authSendLocation(users.get(chatId).getLanguage()), keyboardLocation(users.get(chatId).getLanguage()));
                        states.put(chatId, State.SEND_LOCATION);
                    }
                }
            } else if (currentState == State.SEND_LOCATION) {
                if (update.getMessage().hasLocation()) {
                    users.get(chatId).setLocation(update.getMessage().getLocation());

                    Language language = users.get(chatId).getLanguage();

                    sendMessage(chatId, Message.getDailyMessage(users.get(chatId)), "src/main/resources/weather.jpg", mainPanel(language));

                    states.remove(chatId);
                } else if (update.getMessage().hasText()) {
                    User user = users.get(chatId);
                    String cleanedText = text.replaceAll("\\s+", "%20");

                    user.setCity(cleanedText);

                    sendMessage(chatId, Message.getDailyMessage(users.get(chatId)), "src/main/resources/weather.jpg", mainPanel(users.get(chatId).getLanguage()));
                }
            }
        }
    }

    private boolean isHasUser(Long chatId) {
        return users.containsKey(chatId) && (users.get(chatId).getLocation() != null || users.get(chatId).getCity() != null);
    }
}
