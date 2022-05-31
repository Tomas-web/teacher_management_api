package management.teacher_management_api.ports;

import management.teacher_management_api.domain.core.ExternalUserInfo;

import java.io.IOException;

public interface UserInfoProvider {
    ExternalUserInfo resolve(String accessToken) throws IOException;
}
