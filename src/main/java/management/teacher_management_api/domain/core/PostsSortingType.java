package management.teacher_management_api.domain.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Objects;

@RequiredArgsConstructor
public enum PostsSortingType {
    NEWEST("newest"),
    OLDEST("oldest");

    @Getter private final String name;

    public static PostsSortingType fromName(String name) {
        for (val value : PostsSortingType.values()) {
            if (Objects.equals(value.getName(), name)) {
                return value;
            }
        }

        return null;
    }
}
