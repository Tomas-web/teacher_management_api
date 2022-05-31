package management.teacher_management_api.infrastructure.auth0;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.domain.core.ExternalUserInfo;
import management.teacher_management_api.ports.UserInfoProvider;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoProviderAuth0Impl implements UserInfoProvider {
    private final Auth0Config config;
    private final ObjectMapper objectMapper;

    @Override
    public ExternalUserInfo resolve(String accessToken) throws IOException {
        val restTemplate = new RestTemplate();
        val headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        val url = config.getDomain() + "userinfo";
        val entity = new HttpEntity(headers);
        val response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        val result = ExternalUserInfo.builder();

        val root = objectMapper.readTree(response.getBody());

        return result.id(root.path("sub").asText())
                .displayName(root.path("name").asText())
                .email(root.path("email").asText())
                .avatarUrl(root.path("picture").asText())
                .build();
    }
}
