package org.project.micro.msuser.domain.user.gateway;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.project.micro.msuser.domain.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

public interface JwtGateway {

    Mono<String> getToken(User user);
    Mono<String> getUsernameFromToken(String token);
    Mono<Boolean> isTokenValid(String token, UserDetails userDetails);
    Jws<Claims> parseJwt(String jwtString);
}
