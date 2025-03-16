package uz.weather.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Location;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Long chatId;
    private String userName;
    private String fullName;
    private Location location;
}
