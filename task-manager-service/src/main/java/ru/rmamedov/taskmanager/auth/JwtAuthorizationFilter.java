package ru.rmamedov.taskmanager.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rustam Mamedov
 */

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private static final String JWT_SECRET = "n2r5u8xH+MbQeThWmZq4t7@NcRf";

    private static final String TOKEN_HEADER = "Authorization";

    private static final String TOKEN_PREFIX = "Bearer ";

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        final UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (authentication == null) {
            filterChain.doFilter(request, response);
            return;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(TOKEN_HEADER);

        if (StringUtils.isNotEmpty(token) && token.startsWith(TOKEN_PREFIX)) {
            try {
                final Jws<Claims> parsedToken = Jwts.parser()
                        .setSigningKey(JWT_SECRET.getBytes())
                        .parseClaimsJws(token.replace("Bearer ", ""));

                final String username = parsedToken.getBody().getSubject();

                final List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
                        .get("Roles")).stream()
                        .map(authority -> new SimpleGrantedAuthority((String) authority))
                        .collect(Collectors.toList());

                if (StringUtils.isNotEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                }
            } catch (ExpiredJwtException e) {
                log.warn("Request to parse expired JWT : {} failed : {}", token, e.getMessage());
            } catch (UnsupportedJwtException e) {
                log.warn("Request to parse unsupported JWT : {} failed : {}", token, e.getMessage());
            } catch (MalformedJwtException e) {
                log.warn("Request to parse invalid JWT : {} failed : {}", token, e.getMessage());
            } catch (SignatureException e) {
                log.warn("Request to parse JWT with invalid signature : {} failed : {}", token, e.getMessage());
            } catch (IllegalArgumentException e) {
                log.warn("Request to parse empty or null JWT : {} failed : {}", token, e.getMessage());
            }
        }
        return null;
    }

}
