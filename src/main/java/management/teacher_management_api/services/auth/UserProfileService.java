package management.teacher_management_api.services.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import management.teacher_management_api.domain.core.User;
import management.teacher_management_api.domain.core.UserRole;
import management.teacher_management_api.infrastructure.web.JwtVerifier;
import management.teacher_management_api.ports.UserInfoProvider;
import management.teacher_management_api.ports.persistence.UsersDao;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final JwtVerifier jwtVerifier;
    private final UsersDao usersDao;
    private final UserInfoProvider userInfoProvider;

    public User ensure(String token) {
        DecodedJWT jwt = null;

        try {
            jwt = jwtVerifier.decodeToken(token);
        } catch (JWTVerificationException ex) {
            log.warn("Unable to verify JWT: {}" + token, ex);
            jwt = null;
        }

        if (jwt == null) {
            return null;
        }

        User user = usersDao.findByExternalId(jwt.getSubject());

        if (user == null) {
            user = createUserAndAccount(token);
            log.info("Registered new user id={} ({})", user.getId(), jwt.getSubject());
        }

        return user;
    }

    private User createUserAndAccount(String accessToken) {
        val user = new User();

        try {
            val externalUser = userInfoProvider.resolve(accessToken);

            user.setExternalId(externalUser.getId());
            user.setFullName(externalUser.getDisplayName());
            user.setEmail(externalUser.getEmail());
            user.setPicture(externalUser.getAvatarUrl());
            user.setRoleId(UserRole.STUDENT.getId());
            user.setCreatedAt(LocalDateTime.now());

            usersDao.saveOrUpdate(user);
        } catch (Exception ex) {
            log.error("Unhandled exception while persisting user to the DB", ex);
        }

        return user;
    }
}
