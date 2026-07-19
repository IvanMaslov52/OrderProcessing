package com.example.gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class UserIdFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .flatMap(ctx -> {
                    AbstractAuthenticationToken auth =
                            (AbstractAuthenticationToken) ctx.getAuthentication();

                    if (auth == null || !auth.isAuthenticated()) {
                        return chain.filter(exchange);
                    }

                    Jwt jwt = (Jwt) auth.getPrincipal();

                    String keycloakId = jwt.getSubject();

                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .header("X-User-Id", keycloakId)
                            .build();

                    return chain.filter(
                            exchange.mutate()
                                    .request(mutatedRequest)
                                    .build()
                    );
                })
                .switchIfEmpty(chain.filter(exchange));
    }
    @Override public int getOrder() { return -1; }
}
