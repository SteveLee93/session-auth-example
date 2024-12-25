package com.example.gateway.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SessionFilter implements GlobalFilter, Ordered {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();

        // 공개 API는 세션 체크 제외
        if (isPublicPath(path)) {
            return chain.filter(exchange);
        }

        HttpCookie sessionCookie = exchange.getRequest().getCookies().getFirst("SESSION");
        if (sessionCookie == null || !isValidSession(sessionCookie.getValue())) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 요청에 userId 헤더 추가
        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("X-User-ID", getUserIdFromSession(sessionCookie.getValue()))
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/auth/login") || 
               path.startsWith("/auth/signup") || 
               path.startsWith("/users/signup");
    }

    private boolean isValidSession(String sessionId) {
        return redisTemplate.hasKey("SESSION:" + sessionId);
    }

    private String getUserIdFromSession(String sessionId) {
        return redisTemplate.opsForValue().get("SESSION:" + sessionId);
    }

    @Override
    public int getOrder() {
        return -1; // 다른 필터보다 먼저 실행
    }
} 