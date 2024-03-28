package ru.kazan.clientservice.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.kazan.clientservice.utils.filter.JwtFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtFilter filter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/client/info").authenticated()
                        .requestMatchers("/client/edit/address").authenticated()
                        .requestMatchers("/client/edit/delete/address").authenticated()
                        .requestMatchers("/client/edit/email").authenticated()
                        .requestMatchers("/client/edit/mobile-phone").authenticated()
                        .requestMatchers("/session/token").authenticated()
                        .requestMatchers("/session/password/change").authenticated()
                        .requestMatchers("/session/password/new").authenticated()
                        .anyRequest().permitAll()
                ).exceptionHandling(exc ->
                        exc.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                ).build();

    }

}
