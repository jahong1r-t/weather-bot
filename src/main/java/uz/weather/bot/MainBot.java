package uz.weather.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import uz.weather.entity.Response;
import uz.weather.entity.User;
import uz.weather.entity.enums.State;
import uz.weather.service.ApiService;
import uz.weather.util.Bot;
import uz.weather.util.Button;
import uz.weather.util.Message;
import uz.weather.util.Util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static uz.weather.db.Datasource.*;

public class MainBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            states.putIfAbsent(chatId, State.MAIN_MENU);

            State currentState = states.get(chatId);

            if (currentState == State.MAIN_MENU) {
                switch (text) {
                    case "/start" ->
                            sendMessage(chatId, Message.startMessage + "\n" + formatDate(update.getMessage().getDate()), keyboard(Util.mainMenu));
                    case Button.weather_now_uz -> {
                        if (isHasUser(chatId)) {
                            User user = users.get(chatId);
//                            sendMessage(chatId, );
                        } else {
                            sendMessage(chatId, Message.sendLocation, keyboardLocation());
                            states.put(chatId, State.SEND_LOCATION);
                        }
                    }
                }
            } else if (currentState == State.SEND_LOCATION) {
                if (update.getMessage().hasLocation()) {
                    var from = update.getMessage().getFrom();

                    Double latitude = update.getMessage().getLocation().getLatitude();
                    Double longitude = update.getMessage().getLocation().getLongitude();

                    User user = new User(chatId, from.getUserName() == null ? "No username" : from.getUserName(), from.getFirstName(), update.getMessage().getLocation());
                    users.put(chatId, user);


                    Response information = ApiService.getInformation(longitude, latitude, formatDate(update.getMessage().getDate()));

                    sendMessage(chatId, Util.getWeatherMessage(information));
                }
            }
        }
    }

    private boolean isHasUser(Long chatId) {
        return users.containsKey(chatId);
    }


    public static String formatDate(Integer date) {

        Instant instant = Instant.ofEpochSecond(date);

        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String text) {
        execute(SendMessage.builder()
                .text(text)
                .chatId(chatId)
                .build());
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String text, ReplyKeyboard keyboard) {
        execute(SendMessage.builder()
                .text(text)
                .replyMarkup(keyboard)
                .chatId(chatId)
                .build());
    }

    @Override
    public String getBotUsername() {
        return Bot.USERNAME;
    }

    @Override
    public String getBotToken() {
        return Bot.TOKEN;
    }
}
