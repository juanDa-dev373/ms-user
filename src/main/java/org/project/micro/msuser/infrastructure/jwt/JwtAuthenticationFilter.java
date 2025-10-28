package org.project.micro.msuser.infrastructure.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.micro.msuser.application.dto.Message;
import org.project.micro.msuser.domain.user.gateway.JwtGateway;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtGateway jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String token = extractToken(exchange);
        if (token == null){
            return chain.filter(exchange);
        }
        Jws<Claims> jws;
        try{
            jws = jwtService.parseJwt(token);
        }catch (Exception e){
            log.error("Ocurrio un error: {}", e.getMessage(), e);
            return createResponseError(exchange);
        }

        String username = jws.getBody().getSubject();
        List<String> rawRoles =  jws.getBody().get("rol", List.class);

        List<SimpleGrantedAuthority> authorities = rawRoles.stream()
                .map(Object::toString)
                .map(SimpleGrantedAuthority::new)
                .toList();

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        new User(username, "", authorities),
                        null,
                        authorities
                );

        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(
                        Mono.just(new SecurityContextImpl(authentication))
                ));
    }

    private String extractToken(ServerWebExchange exchange) {
        String header = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
    private Mono<Void> createResponseError(ServerWebExchange exchange) {
        try {
            Message dto = new Message("Invalid token");
            byte[] bytes = objectMapper.writeValueAsBytes(dto);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse()
                    .bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
