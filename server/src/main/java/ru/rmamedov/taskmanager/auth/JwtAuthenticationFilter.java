package ru.rmamedov.taskmanager.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.rmamedov.taskmanager.model.Role;
import ru.rmamedov.taskmanager.model.User;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Rustam Mamedov
 */

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final String JWT_SECRET = "n2r5u8xH+MbQeThWmZq4t7@NcRf";

    private static final String TOKEN_TYPE = "JWT";

    private static final String TOKEN_HEADER = "Authorization";

    private static final String TOKEN_PREFIX = "Bearer ";

    private static final long EXPIRATION_TIME = 432_000_00;

    public JwtAuthenticationFilter(final AuthenticationManager authenticationManager,
                                   String loginUrl) {
        setAuthenticationManager(authenticationManager);
        setFilterProcessesUrl(loginUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            final CredentialsDTO credentials =
                    new ObjectMapper().readValue(req.getInputStream(), CredentialsDTO.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getUsername(),
                            credentials.getPassword(),
                            Collections.emptyList()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res,
                                            FilterChain filterChain, Authentication authentication) {

        final User user = ((User) authentication.getPrincipal());

        final Stream<String> roleNames = user.getRoles().stream().map(Role::getName);

        final String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET.getBytes())
                .setHeaderParam("Token-type", TOKEN_TYPE)
                .setSubject(user.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .claim("Roles", roleNames.collect(Collectors.toList()))
                .compact();

        res.addHeader(TOKEN_HEADER, TOKEN_PREFIX + token);
        res.addHeader("Access-Control-Expose-Headers", "Authorization");
        res.addHeader("Username", user.getUsername());
    }

}
