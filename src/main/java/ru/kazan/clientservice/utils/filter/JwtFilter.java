package ru.kazan.clientservice.utils.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
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
import ru.kazan.clientservice.dto.exception.ExceptionResponse;
import ru.kazan.clientservice.exception.ExceptionEnum;
import ru.kazan.clientservice.service.UserDetail;
import ru.kazan.clientservice.utils.enums.RoleEnum;
import ru.kazan.clientservice.utils.security.JwtProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";

    private static final String REASON_OF_EXCEPTION = "\033[1;97mAn invalid request with error \033[1;31m[ {} ]" +
            " \033[1;97mwas rejected because\n {}";

    private final JwtProvider jwtProvider;

    private final UserDetail userDetail;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String client = null;
        RoleEnum role = null;
        String token = getTokenFromRequest(request);
        try {
            if (token != null && jwtProvider.validateToken(token)) {
                client = jwtProvider.getClientIdFromToken(token).toString();
                role = jwtProvider.getRoleFromToken(token);
            }
        } catch (ExpiredJwtException e) {
            ExceptionResponse responseEntity = getErrorExceptionsJwt(request);

            log.error(REASON_OF_EXCEPTION, responseEntity.getError(), responseEntity.getMessage());

            response.setStatus(Objects.requireNonNull(responseEntity).getError());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(new ObjectMapper().writeValueAsString(responseEntity));
            response.flushBuffer();
            return;
        }

        if (client != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails user = userDetail.loadUserByUsername(client);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    null
            );

            auth.setDetails(role);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }

    private ExceptionResponse getErrorExceptionsJwt(@NonNull HttpServletRequest request) {
        ExceptionEnum exception = ExceptionEnum.UNAUTHORIZED;
        ExceptionResponse response = new ExceptionResponse();

        response.setError(exception.getHttpStatus().value());
        response.setTimestamp(LocalDateTime.now());
        response.setUrl(request.getRequestURI());
        response.setMessage("Not valid token");
        response.setType(exception.getErrorMessage());

        return response;
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearer = request.getHeader(AUTHORIZATION);
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
