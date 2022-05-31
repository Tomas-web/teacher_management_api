package management.teacher_management_api.domain.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Objects;

@RequiredArgsConstructor
public enum HomeworkListType {
    ALL("All"),
    UPCOMING("Upcoming");

    @Getter
    private final String name;

    public static HomeworkListType fromName(String name) {
        for (val value : HomeworkListType.values()) {
            if (Objects.equals(value.getName(), name)) {
                return value;
            }
        }

        return null;
    }
}
