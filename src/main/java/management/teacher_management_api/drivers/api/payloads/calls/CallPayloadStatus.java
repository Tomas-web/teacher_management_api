package management.teacher_management_api.drivers.api.payloads.calls;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CallPayloadStatus {
    STARTED("started"),
    ENDED("ended");

    @Getter
    private final String name;
}
