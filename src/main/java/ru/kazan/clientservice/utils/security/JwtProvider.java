package ru.kazan.clientservice.utils.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kazan.clientservice.model.UserProfile;
import ru.kazan.clientservice.utils.enums.RoleEnum;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;


@Slf4j
@Component
public class JwtProvider {

    @Value("${client-service.security.jwt.secret-key}")
    private String key;

    @Value("${client-service.security.jwt.time-session}")
    private long jwtTimeSession;

    @Value("${client-service.security.jwt.time-access}")
    private long jwtTimeAccess;

    @Value("${client-service.security.jwt.time-refresh}")
    private long jwtTimeRefresh;

    public String genAccessToken(UserProfile user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());

        return buildToken(claims ,user.getClient().getId(), jwtTimeAccess);
    }

    public String genSessionToken(UserProfile user){
        return buildToken(new HashMap<>(), user.getClient().getId(), jwtTimeSession);
    }

    public String genRefreshToken(UserProfile user){
        return buildToken(new HashMap<>(), user.getClient().getId(), jwtTimeRefresh);
    }

    public UUID getClientIdFromToken(String token){
        log.info("Get Client id from token");
        return UUID.fromString(extractClaim(token, Claims::getSubject));
    }

    public RoleEnum extractRoleFromToken(String token) {
        String role = extractClaim(token, claims -> claims.get("role")).toString();
        log.info("Get Client role from token {}", role);
        return RoleEnum.fromText(role);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException expEx) {
            log.error("Token expired {}", extractClaim(token, Claims::getExpiration));
        } catch (MalformedJwtException mjEx) {
            log.error("Malformed jwt: {}", token);
        } catch (Exception e) {
            log.error("invalid token", e);
        }
        return false;
    }

    private Claims extractBodyFromAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String buildToken(Map<String, Object> claims, UUID clientId, long expireTime){
        log.info("Build token on {} for client {}", expireTime, clientId);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(clientId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSecretKey())
                .compact();
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claim = extractBodyFromAllClaims(token);
        return claimsResolver.apply(claim);
    }

    private SecretKey getSecretKey(){
        byte [] byteKey = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(byteKey);
    }

}
