package management.teacher_management_api.infrastructure.auth0;

public interface Auth0Config {
    String getKey();

    String getDomain();

    String getAudience();
}
