package ru.kazan.clientservice.utils.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.kazan.clientservice.service.UserDetail;
import ru.kazan.clientservice.utils.jwt.JwtProvider;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";

    private final JwtProvider jwtProvider;

    private final UserDetail userDetail;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String client = null;
        String role = null;
        String token = getTokenFromRequest(request);
        if (token != null && jwtProvider.validateToken(token)) {
            client = jwtProvider.getClientIdFromToken(token).toString();
            role = jwtProvider.extractRoleFromToken(token);
        }

        if (client != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails user = userDetail.loadUserByUsername(client);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    user.getAuthorities()

            );

            auth.setDetails(role);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
