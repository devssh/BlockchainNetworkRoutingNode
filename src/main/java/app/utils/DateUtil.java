package app.utils;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class DateUtil {
    public static String GetDateTimeNow() {
        return ZonedDateTime.now().toString();
    }
    public static String GetDateTimeTomorrow() {
        return LocalDateTime.now().plusDays(1).toString();
    }
}
