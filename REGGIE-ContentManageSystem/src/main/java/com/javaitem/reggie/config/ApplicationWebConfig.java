package com.javaitem.reggie.config;


import com.javaitem.reggie.common.JacksonObjectMapper;
import com.javaitem.reggie.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.smile.MappingJackson2SmileHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @Author win
 * @create 2022/11/22 21:29
 */
@Configuration
public class ApplicationWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
                .excludePathPatterns(

                        "/backend/api/**",
                        "/backend/images/**",
                        "/backend/plugins/**",
                        "/backend/js/**",
                        "/backend/styles/**",
                        "/backend/page/login/**",
                        "/backend/employee/login",

                        "/front/page/login.html",
                        "/front/fonts/**",
                        "/front/api/**",
                        "/front/images/**",
                        "/front/js/**",
                        "/front/styles/**",
                        "/front/user/login",
                        "/front/user/sms"
                        );
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        converters.add(0,messageConverter);
    }
}
