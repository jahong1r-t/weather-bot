package uz.weather.db;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.weather.entity.User;
import uz.weather.entity.enums.State;
import uz.weather.util.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Datasource {
    public static Map<Long, State> states = new HashMap<>();
    public static Map<Long, User> users = new HashMap<>();

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

    public static ReplyKeyboardMarkup keyboardLocation() {

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();

        KeyboardButton locationButton = new KeyboardButton();
        locationButton.setText("📍 Lokatsiya yuborish");
        locationButton.setRequestLocation(true);

        row.add(locationButton);
        keyboard.add(row);

        ReplyKeyboardMarkup reply = new ReplyKeyboardMarkup();

        reply.setKeyboard(keyboard);
        reply.setResizeKeyboard(true);


        return reply;
    }
}
