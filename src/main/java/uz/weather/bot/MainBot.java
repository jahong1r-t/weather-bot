package uz.weather.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import uz.weather.service.AuthService;
import uz.weather.util.Bot;

import java.io.File;


public class MainBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        new AuthService().service(update);
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String message) {
        execute(SendMessage.builder().chatId(chatId).text(message).build());
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String caption, String path, ReplyKeyboard keyboard) {
        execute(SendPhoto.builder().chatId(chatId).caption(caption).replyMarkup(keyboard).photo(new InputFile(new File(path))).build());
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String caption, String path) {
        execute(SendPhoto.builder().chatId(chatId).caption(caption).photo(new InputFile(new File(path))).build());
    }

    @SneakyThrows
    public void sendMessage(Long chatId, String message, ReplyKeyboard keyboard) {
        execute(SendMessage.builder().chatId(chatId).text(message).replyMarkup(keyboard).build());
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
