package ru.rmamedov.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Rustam Mamedov
 */

@Component
public class LoginAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log =
            LoggerFactory.getLogger(LoginAccessDeniedHandler.class);

    @Override
    public void handle(final HttpServletRequest request,
                       final HttpServletResponse response,
                       final AccessDeniedException ex) throws IOException {
        final String host = "http://localhost:30000";

        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {

            log.info("User '" + auth.getName() +
                    "' - Was trying to access protected resource: '" + request.getRequestURI() + "'");
        }
        response.sendRedirect(host + "/#/access-denied");
//      request.getContextPath() +
    }
}
