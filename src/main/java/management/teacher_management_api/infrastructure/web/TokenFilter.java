package management.teacher_management_api.infrastructure.web;

import lombok.val;
import management.teacher_management_api.domain.core.User;
import management.teacher_management_api.services.auth.AuthenticationService;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class TokenFilter extends OncePerRequestFilter {
    private static final String PROFILE_GET_URL = "/profile";

    private final AuthenticationService authenticationService;

    public TokenFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        response.addHeader(
                HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, DELETE, PUT, OPTIONS");
        response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Content-Type, Authorization");

        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            logger.debug("this is OPTIONS to " + request.getServletPath());
            filterChain.doFilter(request, response);
        } else {
            String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

            if (!StringUtils.isEmpty(auth)) {
                auth = auth.replace("Bearer ", "");

                Authentication token;
                if (request.getServletPath().equals(PROFILE_GET_URL)
                        && HttpMethod.GET.matches(request.getMethod())) {
                    logger.debug("this is GET /profile");

                    token =
                            new UsernamePasswordAuthenticationToken(
                                    null, auth, Arrays.asList(new SimpleGrantedAuthority("user")));
                    SecurityContextHolder.getContext().setAuthentication(token);
                } else {
                    try {
                        token = authenticationService.authenticateWithToken(auth);
                    } catch (Exception ex) {
                        logger.error("Token: " + request.getHeader(HttpHeaders.AUTHORIZATION), ex);
                        throw ex;
                    }
                }

                if (token == null) {
                    response.sendError(HttpStatus.FORBIDDEN.value());
                } else {
                    try {
                        SecurityContextHolder.getContext().setAuthentication(token);

                        val user = (User) token.getPrincipal();
                        if (user != null) {
                            MDC.put("user.id", Long.toString(user.getId()));
                        }

                        filterChain.doFilter(request, response);
                    } finally {
                        MDC.remove("user.id");
                    }
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }
}
