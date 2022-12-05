package com.javaitem.reggie.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author win
 * @create 2022/11/22 21:25
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        String s = split[1];
        if (s.equals("backend")){
            Long employee = (Long)request.getSession().getAttribute("employee");
            if (employee==null){
                response.sendRedirect("/backend/page/login/login.html");
                return false;
            }
            return true;

        }else {
            Long user = (Long)request.getSession().getAttribute("user");
            if (user==null){
                response.sendRedirect("/front/page/login.html");
                return false;
            }
            return true;
        }


    }
}
