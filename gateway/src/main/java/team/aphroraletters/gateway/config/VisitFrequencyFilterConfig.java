package team.aphroraletters.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import team.aphroraletters.gateway.util.RedisUtils;
import team.aphroraletters.gateway.util.RequestUtils;

@Configuration
public class VisitFrequencyFilterConfig {

    @Autowired
    private RedisUtils redisUtils;

    @Bean
    public RouteLocator visitRouteLocator(RouteLocatorBuilder builder, GatewayFilter visitFrequencyFilter) {
        return builder.routes()
                .route("visit_frequency_article_route", r -> r.path("/article/**")
                        .filters(f -> f.filter(visitFrequencyFilter))
                        .uri("lb://article"))
                .route("visit_frequency_library_route", r -> r.path("/library/**")
                        .filters(f -> f.filter(visitFrequencyFilter))
                        .uri("lb://library"))
                .build();
    }

    /**
     * 限制单位时间请求次数
     *
     * @return
     */
    @Bean
    public GatewayFilter visitFrequencyFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            int limit_count = 128; // 最大请求次数
            int limit_time = 60; // 访问限制的单位时间（1min）

            String ip = RequestUtils.getRemoteIp(request);
            String url = request.getURI().getPath();
            String key = "req-frequency:ip-".concat(ip);
            try {
                String value = redisUtils.get(key);
                if (value == null) {
                    value = "1_" + System.currentTimeMillis();
                    redisUtils.set(key, value, limit_time);
                } else {
                    String[] s = value.split("_");
                    int count = Integer.parseInt(s[0]);

                    if (count > limit_count) {
                        System.out.printf("用户IP[%s], 访问地址[%s], 超过了限定的次数[%s]。%n", ip, url, limit_count);
                        throw new Exception("429: Too Many Requests");
                    }

                    value = (count + 1) + "_" + s[1];
                    long last = limit_time - (System.currentTimeMillis() - Long.parseLong(s[1])) / 1000;
                    if (last > 0) {
                        redisUtils.set(key, value, limit_time);
                    }
                }
            } catch (Exception e) {
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(("The number of visits is abnormal, please try again later!").getBytes())));
            }

            return chain.filter(exchange);
        };
    }
}
