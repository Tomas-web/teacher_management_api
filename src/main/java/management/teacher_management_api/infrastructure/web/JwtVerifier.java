package management.teacher_management_api.infrastructure.web;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtVerifier {
    public DecodedJWT decodeToken(String token) {
        try {
            return JWT.decode(token);
        } catch (JWTVerificationException ex) {
            log.error("Unable to verify JWT token: {}", token, ex);
        } catch (Exception ex) {
            log.error("Unhandled exception while verifying JWT token", ex);
        }

        return null;
    }
}
