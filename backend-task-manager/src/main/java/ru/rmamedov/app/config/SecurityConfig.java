package ru.rmamedov.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.rmamedov.app.service.interfaces.IUserService;

/**
 * @author Rustam Mamedov
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private IUserService userService;

    private LoginAccessDeniedHandler accessDeniedHandler;

    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    public SecurityConfig(LoginAccessDeniedHandler accessDeniedHandler, PersistentTokenRepository persistentTokenRepository) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.persistentTokenRepository = persistentTokenRepository;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                    .antMatchers("/webjars/**", "/resources/**", "/js/**", "/css/**", "/js/**");
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().disable();

        http
                .httpBasic().disable();

        http
                .authorizeRequests()

                    .antMatchers("/#/", "/#/login", "/#/registration")
                        .permitAll()

                    .antMatchers("/api/**").access("isAuthenticated()")

                    .antMatchers(HttpMethod.POST, "/api/user/saveUnderUserAndProject").access("permitAll()")
                    .antMatchers(HttpMethod.GET, "/api/user/all").access("hasRole('ROLE_ADMIN')")

                    .and()
                        .formLogin()
                            .loginProcessingUrl("/login")
                            .defaultSuccessUrl("/", true)
                            .failureUrl("/#/login?error")
                                .permitAll()

                    .and()
                        .logout()
                            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                            .logoutSuccessUrl("/#/login?logout")
                            .deleteCookies("JSESSIONID")
                            .invalidateHttpSession(true)
                            .clearAuthentication(true)
                                .permitAll()

                    .and()
                        .rememberMe()
                            .tokenRepository(persistentTokenRepository)
                            .tokenValiditySeconds(600)
                            .key("123qwe")

                    .and()
                        .exceptionHandling()
                        .accessDeniedHandler(accessDeniedHandler);

        http
                .sessionManagement()
                    .invalidSessionUrl("/#/login")
                    .maximumSessions(1)
                    .maxSessionsPreventsLogin(true)
                    .sessionRegistry(sessionRegistry());
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
                = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}
