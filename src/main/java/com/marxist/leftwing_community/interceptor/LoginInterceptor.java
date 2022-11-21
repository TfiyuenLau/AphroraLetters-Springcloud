package com.marxist.leftwing_community.interceptor;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {
    //在preHandle方法中进行登录判断
    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {
        // 静态资源不拦截
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        HttpSession session = request.getSession();
        String loginStatus = (String) session.getAttribute("login_status");//获取是否登录
        String adminName = (String)session.getAttribute("adminName");//获取储存的session
        if(loginStatus==null || adminName==null){
            //System.err.println("请先登陆!");
            response.sendRedirect(request.getContextPath() + "/admin/login_page");

            return false;
        }

        return true;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) throws Exception {
        //System.out.println("执行了TestInterceptor的postHandle方法");
    }


    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {

    }
}
