package management.teacher_management_api.domain.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.Objects;

@RequiredArgsConstructor
public enum UserRole {
    STUDENT(1),
    TEACHER(2);

    @Getter private final Integer id;

    public static UserRole fromId(Integer id) {
        for (val value : UserRole.values()) {
            if (Objects.equals(value.getId(), id)) {
                return value;
            }
        }

        return null;
    }
}
