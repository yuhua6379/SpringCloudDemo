package com.springcloud.demo.gateway;

import com.springcloud.demo.gateway.dto.VerifyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class AuthFilter implements GlobalFilter, Ordered {

    @Value("${auth-filter.open}")
    private boolean open;

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 放行所有swagger-ui页面请求
        URI uri = exchange.getRequest().getURI();
        try {
            String[] paths = uri.getPath().split("/");
            if (paths[2].startsWith("swagger")) {
                logger.debug("Release the request to swagger. URI: {}", uri);
                return chain.filter(exchange);
            } else if (paths[2].startsWith("v") && paths[3].equals("api-docs")) {
                logger.debug("Release the request to swagger. URI: {}", uri);
                return chain.filter(exchange);
            } else if (paths[2].equals("webjars")) {
                logger.debug("Release the request to swagger. URI: {}", uri);
                return chain.filter(exchange);
            }
        } catch (Exception e) {
            logger.error("Can't handle this uri: {}", uri);
        }

        // 从请求头获取到Authorization头
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        // 检查是否有 "Authorization" header 以及是否以 "Bearer" 开头

        if (token == null) {
            // 不需要校验
            return chain.filter(exchange);
        }


        if (!token.startsWith("Bearer ")) {
            // 非他的header直接抛弃
            return this.setUnauthorized(exchange);
        } else {
            // 非常遗憾，因为gateway是响应式的，所以不能简单使用feign，只能用比较复杂的WebClient实现
            WebClient webClient = webClientBuilder.build();

            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            // 这里//auth是使用了注册在Eureka上的服务名
                            .path("//auth/user/verify")
                            .queryParam("token", token)
                            .build()) // 使用服务名而不是具体的URL
                    .retrieve()
                    .bodyToMono(VerifyDto.class)
                    .flatMap(verifyDto -> {
                        String userName = verifyDto.getUserName();
                        if (userName == null || userName.isEmpty()) {
                            // 返回数据失败则直接定义为未认证
                            return this.setUnauthorized(exchange);
                        } else {
                            // 把认证后返回的用户名放置到X-UserName自定义头内，传递给后续转发的服务
                            ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                    .header("X-UserName", userName)
                                    .build();
                            ServerWebExchange mutatedExchange = exchange.mutate().request(mutatedRequest).build();
                            return chain.filter(mutatedExchange);
                        }
                    })
                    .onErrorResume(e -> {
                        // 出现调用远程服务时的错误则直接往上抛
                        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                        logger.error("There is an error while calling verify service.", e);
                        return exchange.getResponse().setComplete();
                    });
        }
    }

    public Mono<Void> setUnauthorized(ServerWebExchange exchange) {
        // 如果JWT无效，或者根本没有JWT，则返回401 Unauthorized
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        DataBuffer buffer = response.bufferFactory().wrap("Unauthorized".getBytes());

        // 对无效请求头返回Unauthorized 401
        return response.writeWith(Mono.just(buffer));
    }

    public int getOrder() {
        return 0;
    }
}
