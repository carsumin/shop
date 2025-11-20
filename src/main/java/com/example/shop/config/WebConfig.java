package com.example.shop.config;

import com.example.shop.aop.UnAuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.http.HttpHeaders;

import java.util.Enumeration;

@Configuration
public class WebConfig {
    public WebMvcConfigurer corsConfig(){
        return new WebMvcConfigurer() {

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                WebMvcConfigurer.super.addInterceptors(registry);
                registry.addInterceptor(new HandlerInterceptor() {
                    @Override
                    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//                        request.getContextPath().startsWith("/api/v1");
//                        Enumeration<String> aaa = request.getHeaders(HttpHeaders.AUTHORIZATION);
//                        String token = aaa.nextElement().replaceAll("Berear", "");
//                        new UnAuthException("unauthorize user");
                        System.out.println(request.getContextPath());
                        System.out.println(request.getHeader(HttpHeaders.AUTHORIZATION));
                        return HandlerInterceptor.super.preHandle(request, response, handler);
                    }

                    @Override
                    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
                        System.out.println(response.getStatus());
                        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
                    }
                });
            }

            // 브라우저의 CORS OPTIONS 요청을 허용하여
            // PUT/PATCH/DELETE 요청 시 발생하는 CORS 오류를 방지한다
            // 프로젝트에서 필수적으로 넣기
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/v3/**");
                registry.addMapping("/api/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
