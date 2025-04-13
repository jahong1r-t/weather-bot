package uz.weather.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import uz.weather.bot.MainBot;
import uz.weather.entity.User;
import uz.weather.util.Message;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static uz.weather.db.Datasource.users;

@Slf4j
public class DailyWeatherService extends MainBot {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public void startDailyWeatherService() {
        scheduleDailyTask();
    }

    @SneakyThrows
    private void scheduleDailyTask() {
        ZoneId defaultZone = ZoneId.of("Asia/Tashkent");
        LocalDateTime now = LocalDateTime.now(defaultZone);
        LocalDateTime nextRun = now.withHour(7).withMinute(0).withSecond(0).withNano(0);
        if (now.isAfter(nextRun) || now.isEqual(nextRun)) {
            nextRun = nextRun.plusDays(1);
        }

        long initialDelay = Duration.between(now, nextRun).getSeconds();

        Runnable task = () -> {
            try {
                log.info("Sending daily weather updates to subscribed users at 07:00");
                sendDailyWeatherToSubscribedUsers();
            } catch (Exception e) {
                log.error("Error sending daily weather updates: {}", e.getMessage());
            }
        };

        scheduler.scheduleAtFixedRate(task, initialDelay, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);
    }

    @SneakyThrows
    private void sendDailyWeatherToSubscribedUsers() {
        for (var entry : users.entrySet()) {
            Long chatId = entry.getKey();
            User user = entry.getValue();

            if (user.isSubscribed() && (user.isCityFound() || user.getLocation() != null)) {
                ZoneId userZone = user.getZoneId() != null ? user.getZoneId() : ZoneId.of("Asia/Tashkent");
                LocalDateTime userTime = LocalDateTime.now(userZone);

                if (userTime.getHour() == 7 && userTime.getMinute() == 0) {
                    Message.DailyWeatherInfo weatherInfo = Message.getDailyMessage(user);
                    sendMessage(chatId, weatherInfo.message(), weatherInfo.imagePath());
                    log.info("Daily weather sent to subscribed user: {}", chatId);
                }
            }
        }
    }

    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(10, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
}
