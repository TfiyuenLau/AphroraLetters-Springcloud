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
public class CommentFrequencyFilterConfig {

    @Autowired
    private RedisUtils redisUtils;

    @Bean
    public RouteLocator commentRouteLocator(RouteLocatorBuilder builder, GatewayFilter commentFrequencyFilter) {
        return builder.routes()
                .route("comment_frequency_article_route", r -> r.path("/article/insertArticleComment")
                        .filters(f -> f.filter(commentFrequencyFilter))
                        .uri("lb://article"))
                .build();
    }

    /**
     * 限制单位时间评论次数
     *
     * @return
     */
    @Bean
    public GatewayFilter commentFrequencyFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            int limitCount = 2; // 最大评论次数
            int limitTime = 60; // 访问限制的单位时间（1min）

            try {
                String ip = RequestUtils.getRemoteIp(request);
                String url = request.getURI().getPath();
                String key = "comment-limit:ip-".concat(ip);

                String value = redisUtils.get(key);
                if (value == null) {
                    value = "1_" + System.currentTimeMillis();
                    redisUtils.set(key, value, limitTime);
                } else {
                    String[] s = value.split("_");
                    int count = Integer.parseInt(s[0]);

                    if (count > limitCount) {
                        System.out.printf("用户IP[%s], 访问地址[%s], 超过了限定的次数[%s]", ip, url, limitCount);
                        throw new Exception("当前ip单位时间评论次数超过限制!");
                    }

                    value = (count + 1) + "_" + s[1];
                    long last = limitTime - (System.currentTimeMillis() - Long.parseLong(s[1])) / 1000;
                    if (last > 0) {
                        redisUtils.set(key, value, limitTime);
                    }
                }
            } catch (Exception e) {
                response.setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                response.getHeaders().setContentType(MediaType.TEXT_PLAIN);
                return response.writeWith(Mono.just(response.bufferFactory().wrap(("The number of comments per unit time of the current IP exceeds the limit!").getBytes())));
            }

            return chain.filter(exchange);
        };
    }
}
