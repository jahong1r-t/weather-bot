package uz.weather.db;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.weather.entity.User;
import uz.weather.entity.enums.Language;
import uz.weather.entity.enums.State;
import uz.weather.util.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Datasource {
    public static Map<Long, State> states = new HashMap<>();
    public static Map<Long, User> users = new HashMap<>();

    @SneakyThrows
    public static ReplyKeyboardMarkup keyboard(String[][] buttons) {
        List<KeyboardRow> rows = new ArrayList<>();

        for (String[] button : buttons) {
            KeyboardRow keyboardRow = new KeyboardRow();
            for (String s : button) {
                keyboardRow.add(s);
            }
            rows.add(keyboardRow);
        }
        ReplyKeyboardMarkup reply = new ReplyKeyboardMarkup();
        reply.setResizeKeyboard(true);
        reply.setKeyboard(rows);

        return reply;
    }

    @SneakyThrows
    public static ReplyKeyboardMarkup keyboardLocation(Language lang) {

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        KeyboardButton locationButton = new KeyboardButton();
        locationButton.setText(lang == Language.UZBEK ? Button.location_uz : lang == Language.ENGLISH ? Button.location_en : Button.location_ru);
        locationButton.setRequestLocation(true);

        row.add(locationButton);
        keyboard.add(row);

        ReplyKeyboardMarkup reply = new ReplyKeyboardMarkup();

        reply.setKeyboard(keyboard);
        reply.setResizeKeyboard(true);

        return reply;
    }
}
