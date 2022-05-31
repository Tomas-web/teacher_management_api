package management.teacher_management_api.drivers.api;

import lombok.val;
import management.teacher_management_api.domain.core.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public class ApiUtils {
    private ApiUtils() {}

    public static long getAuthenticatedUserId() {
        val auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        } else {
            val user = (User) auth.getPrincipal();
            return user.getId();
        }
    }
}
