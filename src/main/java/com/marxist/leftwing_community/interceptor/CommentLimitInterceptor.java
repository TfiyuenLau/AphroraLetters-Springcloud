package com.marxist.leftwing_community.interceptor;

import com.marxist.leftwing_community.util.CacheUtils;
import com.marxist.leftwing_community.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class CommentLimitInterceptor implements HandlerInterceptor {

    /**
     * 拦截器：限制ip对应的用户的单位时间最评论次数
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param object
     * @return
     */
    @Override
    public boolean preHandle(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull Object object) throws IOException {

        try {
            int limit_count = 2;//最大评论次数
            int limit_time = 60 * 1000;//访问限制的单位时间（1min）

            String ip = RequestUtils.getRemoteIp(httpServletRequest);
            String url = httpServletRequest.getRequestURL().toString();
            String key = "comment_limit_".concat(ip);

            String cache = (String) CacheUtils.get(key);
            if (null == cache) {
                String value = "1_" + System.currentTimeMillis();
                CacheUtils.put(key, value, limit_time);
            } else {
                String value = (String) cache;
                String[] s = value.split("_");
                int count = Integer.parseInt(s[0]);

                if (count > limit_count) {
                    log.error("用户IP[{}], 访问地址[{}], 超过了限定的次数[{}]", ip, url, limit_count);
                    throw new Exception("当前ip单位时间评论次数超过限制!");
                }

                value = (count + 1) + "_" + s[1];
                long last = limit_time - (System.currentTimeMillis() - Long.parseLong(s[1]));
                if (last > 0) {
                    CacheUtils.put(key, value, limit_time);
                }
            }

        } catch (Exception e) {
            //跳转至警告页面
            httpServletResponse.setContentType("text/html; charset=utf-8");
            httpServletResponse.getWriter().write("<h3 align='center'>评论次数异常！请一分钟后再试...</h3><script>setTimeout(function(){window.history.go(-1);},5000);</script>");

            return false;
        }

        return true;
    }
}
