package management.teacher_management_api.infrastructure.spring;

import lombok.Data;
import management.teacher_management_api.infrastructure.auth0.Auth0Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class Auth0ConfigImpl implements Auth0Config {
    @Value("${management.integrations.auth0.key}")
    private String key;

    @Value("${management.integrations.auth0.iss}")
    private String domain;

    @Value("${management.integrations.auth0.aud}")
    private String audience;
}
