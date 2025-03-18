package uz.weather.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Location;
import uz.weather.entity.enums.Language;

import java.time.ZoneId;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private Long chatId;
    private String userName;
    private String fullName;
    private Location location;
    private String city;
    private boolean isSubscribed;
    private boolean isCityFound;
    private Language language;
    private ZoneId zoneId;
}
