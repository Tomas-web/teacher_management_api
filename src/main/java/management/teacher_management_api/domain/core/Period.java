package management.teacher_management_api.domain.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.time.LocalDateTime;
import java.util.Objects;

@RequiredArgsConstructor
public enum Period {
    ONE_WEEK("1week", 7),
    TWO_WEEKS("2weeks", 14),
    ONE_MONTH("1month", 30),
    SIX_MONTHS("6months", 180),
    ONE_YEAR("1year", 360);

    @Getter
    private final String name;

    @Getter
    private final int numOfDays;

    public static Period fromName(String value) {
        for (val val : Period.values()) {
            if (Objects.equals(val.getName(), value)) {
                return val;
            }
        }

        return null;
    }

    public LocalDateTime ago() {
        return LocalDateTime.now().minusDays(this.getNumOfDays()).toLocalDate().atStartOfDay();
    }
}
