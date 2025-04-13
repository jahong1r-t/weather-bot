package uz.weather.service;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.weather.bot.MainBot;
import uz.weather.entity.User;
import uz.weather.entity.enums.Language;
import uz.weather.entity.enums.State;
import uz.weather.util.Message;
import uz.weather.util.Util;

import static uz.weather.db.Datasource.*;
import static uz.weather.util.Button.*;
import static uz.weather.util.Message.*;

public class UserService extends MainBot {

    @SneakyThrows
    public void service(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        states.putIfAbsent(chatId, State.MAIN_MENU);
        Language lang = users.get(chatId).getLanguage();
        User user = users.get(chatId);
        State currentState = states.get(chatId);

        if (!update.getMessage().hasLocation() && isMainMenuCommand(text) && currentState != State.MAIN_MENU && currentState != State.SETTINGS_PANEL) {
            states.put(chatId, State.MAIN_MENU);
            currentState = State.MAIN_MENU;
        }

        if (currentState == State.MAIN_MENU) {
            switch (text) {
                case "/start" -> sendMessage(chatId, Message.mainMenuMsg(lang), mainPanel(lang));
                case today_uz, today_en, today_ru -> {
                    Message.DailyWeatherInfo weatherInfo = Message.getDailyMessage(user);
                    if (user.isCityFound()) {
                        sendMessage(chatId, weatherInfo.message(), weatherInfo.imagePath());
                    } else {
                        sendMessage(chatId, authSendLocation(lang), keyboardLocation(lang));
                        states.put(chatId, State.SEND_LOCATION);
                    }
                }
                case subscribe_uz, subscribe_en, subscribe_ru -> {
                    if (user.isSubscribed()) {
                        sendMessage(chatId, unSubscribedMsg(lang));
                        user.setSubscribed(false);
                    } else {
                        sendMessage(chatId, subscribedMsg(lang));
                        user.setSubscribed(true);
                    }
                }

                case week_uz, week_en, week_ru -> {
                    if (user.isCityFound()) {
                        sendMessage(chatId, getWeeklyMessage(user), "src/main/resources/weather.jpg");
                    } else {
                        sendMessage(chatId, authSendLocation(lang), keyboardLocation(lang));
                        states.put(chatId, State.SEND_LOCATION);
                    }
                }
                case search_uz, search_en, search_ru -> {
                    sendMessage(chatId, searchMsg(lang));
                    states.put(chatId, State.SEARCH_LOCATION);
                }
                case settings_en, settings_uz, settings_ru -> {
                    sendMessage(chatId, settingsMenuMsg(lang), settingsPanel(lang));
                    states.put(chatId, State.SETTINGS_PANEL);
                }
            }
        } else if (currentState == State.SEARCH_LOCATION) {
            Message.DailyWeatherInfo info = Message.getDailyMessage(User.builder().language(lang).city(text).build());
            sendMessage(chatId, info.message(), info.imagePath());
            states.remove(chatId);
        } else if (currentState == State.SEND_LOCATION) {
            if (update.getMessage().hasLocation()) {
                user.setLocation(update.getMessage().getLocation());
                Message.DailyWeatherInfo weatherInfo = Message.getDailyMessage(user);
                sendMessage(chatId, weatherInfo.message(), weatherInfo.imagePath(), mainPanel(lang));
                states.remove(chatId);
            } else if (update.getMessage().hasText()) {
                String cleanedText = text.replaceAll("\\s+", "%20");
                user.setCity(cleanedText);
                Message.DailyWeatherInfo weatherInfo = Message.getDailyMessage(user);
                sendMessage(chatId, weatherInfo.message(), weatherInfo.imagePath(), mainPanel(lang));
                states.remove(chatId);
            }
        } else if (currentState == State.CHANGE_LOCATION) {
            if (update.getMessage().hasLocation()) {
                user.setLocation(update.getMessage().getLocation());
                user.setCity(null);
                sendMessage(chatId, changeLocationSuccessMsg(lang), mainPanel(lang));
                states.put(chatId, State.MAIN_MENU);
            } else if (update.getMessage().hasText()) {
                user.setLocation(null);
                String cleanedText = text.replaceAll("\\s+", "%20");
                user.setCity(cleanedText);
                sendMessage(chatId, changeLocationSuccessMsg(lang), mainPanel(lang));
                states.put(chatId, State.MAIN_MENU);
            }
        } else {
            Language language = text.equals(UZ) ? Language.UZBEK : text.equals(EN) ? Language.ENGLISH : Language.RUSSIAN;
            if (currentState == State.SETTINGS_PANEL) {
                switch (text) {
                    case backEn, backRu, backUz -> {
                        sendMessage(chatId, Message.mainMenuMsg(lang), mainPanel(lang));
                        states.put(chatId, State.MAIN_MENU);
                    }
                    case changeLangUz, changeLangEn, changeLangRu -> {
                        sendMessage(chatId, changeLangMsg(lang), keyboard(Util.languageMenu));
                        states.put(chatId, State.CHANGE_LANGUAGE);
                    }
                    case UZ, EN, RU -> {
                        user.setLanguage(language);
                        sendMessage(chatId, change_lang_success_msg(language), mainPanel(language));
                        states.put(chatId, State.MAIN_MENU);
                    }
                    case changeLocationUz, changeLocationEn, changeLocationRu -> {
                        sendMessage(chatId, authSendLocation(lang), keyboardLocation(lang));
                        states.put(chatId, State.CHANGE_LOCATION);
                    }
                }
            } else if (currentState == State.CHANGE_LANGUAGE) {
                if (text.equals(UZ) || text.equals(EN) || text.equals(RU)) {
                    user.setLanguage(language);
                    sendMessage(chatId, change_lang_success_msg(language), mainPanel(language));
                    states.put(chatId, State.MAIN_MENU);
                }
            }
        }
    }

    private boolean isMainMenuCommand(String text) {
        return text.equals(today_uz) || text.equals(today_en) || text.equals(today_ru) ||
                text.equals(week_uz) || text.equals(week_en) || text.equals(week_ru) ||
                text.equals(search_uz) || text.equals(search_en) || text.equals(search_ru) ||
                text.equals(settings_uz) || text.equals(settings_en) || text.equals(settings_ru) ||
                text.equals("/start");
    }
}
