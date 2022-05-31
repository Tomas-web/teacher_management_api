package management.teacher_management_api.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

@Slf4j
@UtilityClass
public class DateTimeUtils {
    public static OffsetDateTime toOffsetDateTime(LocalDateTime dateTime) {
        return dateTime == null ? null : OffsetDateTime.of(dateTime, ZoneOffset.UTC);
    }

    public static OffsetTime toOffsetTime(LocalTime time) {
        return time == null ? null : OffsetTime.of(time, ZoneOffset.UTC);
    }
}
