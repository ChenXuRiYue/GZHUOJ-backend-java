package com.gzhuoj.gateway.filter;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.AntPathMatcher;
import com.gzhuoj.gateway.config.AuthProperties;
import com.gzhuoj.gateway.utils.JwtTool;
import common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final JwtTool jwtTool;
    private final AuthProperties authProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public GatewayFilter apply(Object config) {
        return new GatewayFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
                // 获取request
                ServerHttpRequest request = exchange.getRequest();

                // 使用hutools工具包判断是否在白名单中
                if(checkExclude(String.valueOf(request.getPath()))){
                    return chain.filter(exchange);
                }

                // 获取请求头 中校验的token
                String token = null;
                List<String> headers = request.getHeaders().get("authorization");
                if(CollUtil.isNotEmpty(headers)){
                    token = headers.get(0);
                }
                // 校验token
                String userId = null;
                try{
                    userId = jwtTool.parseToken(token);
                }catch(UnauthorizedException e){
                    ServerHttpResponse response = exchange.getResponse();
                    return response.setComplete();
                }
                // TODO  传递用户信息到下游服务中
                System.out.println("userId = " + userId);
                String userInfo = userId.toString();
                ServerWebExchange ex = exchange.mutate()
                        .request(b -> b.header("userId", userInfo))
                        .build();
                return chain.filter(ex);
            }
        };
    }
    private boolean checkExclude(String path){
        List<String> excludePaths = authProperties.getExcludePaths();
        for(String str : excludePaths){
            if(str.equals(path)){
                return true;
            }
        }
        return false;
    }
}
