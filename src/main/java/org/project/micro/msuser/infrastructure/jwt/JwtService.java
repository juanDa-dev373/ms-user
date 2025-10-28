package org.project.micro.msuser.infrastructure.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.micro.msuser.domain.user.User;
import org.project.micro.msuser.domain.user.gateway.JwtGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
@Slf4j
@Service
public class JwtService implements JwtGateway {


    @Value("${app.jwt.expiration-seconds}")
    private long expirationSeconds;

    private final SecretKey secretKey;

    public JwtService(@Value("${app.jwt.secret}") String secret){
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }


    @Override
    public Mono<String> getToken(User user) {
        Map <String, Object> map = new HashMap<>();
        map.put("rol", List.of(user.getRole().name()));
        return getToken(map, user);
    }

    private Mono<String> getToken(Map<String, Object> extraClaims, User user) {
        final Date now = new Date();
        final Date exp = new Date(now.getTime() + (expirationSeconds * 1000));
        return Mono.just(
                Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact());
    }

    @Override
    public Mono<String> getUsernameFromToken(String token) {
        return Mono.fromCallable(() -> getClaim(token, Claims::getSubject))
                .onErrorResume(e -> Mono.empty());
    }

    @Override
    public Mono<Boolean> isTokenValid(String token, UserDetails userDetails) {
        return getUsernameFromToken(token)
                .map(username -> username.equals(userDetails.getUsername()) && !isTokenExpired(token))
                .defaultIfEmpty(false);
    }

    @Override
    public Jws<Claims> parseJwt(String jwtString) throws ExpiredJwtException,
            UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {
        JwtParser jwtParser = Jwts.parserBuilder().setSigningKey( secretKey ).build();
        log.info("Parseando JWT");
        return jwtParser.parseClaimsJws(jwtString);
    }

    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
