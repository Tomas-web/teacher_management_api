package management.teacher_management_api.services.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.val;
import management.teacher_management_api.domain.Utils;
import management.teacher_management_api.domain.core.User;
import management.teacher_management_api.infrastructure.hibernate.TransactionManager;
import management.teacher_management_api.infrastructure.web.JwtVerifier;
import org.hibernate.criterion.Restrictions;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtVerifier jwtVerifier;
    private final EntityManagerFactory entityManagerFactory;
    private final TransactionManager txManager;

    public Authentication authenticateWithToken(String token) {
        val decodedJwt = jwtVerifier.decodeToken(token);

        if (decodedJwt == null) {
            return null;
        }

        return txManager.returnFromTx(
                status -> {
                    val session = Utils.currentSession(entityManagerFactory);

                    String externalId = parseSub(decodedJwt);
                    if (externalId == null) {
                        return null;
                    }

                    val user =
                            (User)
                                    session.createCriteria(User.class)
                                            .add(Restrictions.eq("externalId", externalId))
                                            .setMaxResults(1)
                                            .uniqueResult();

                    if (user != null) {
                        val auth =
                                new UsernamePasswordAuthenticationToken(
                                        user,
                                        token,
                                        Arrays.asList(new SimpleGrantedAuthority("user")));
                        return auth;
                    }

                    return null;
                });
    }

    private String parseSub(DecodedJWT token) {
        return token.getSubject();
    }
}
