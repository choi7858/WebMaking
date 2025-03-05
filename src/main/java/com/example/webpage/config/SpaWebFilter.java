package com.example.webpage.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
@WebFilter("/*")
public class SpaWebFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI();

        // API, 업로드 경로, 또는 파일 확장자가 포함된 경로는 그대로 진행
        if (path.startsWith("/api") || path.startsWith("/uploads") || path.contains(".")) {
            chain.doFilter(request, response);
        } else {
            // 그 외 요청은 index.html로 포워딩하여 클라이언트 라우팅 처리
            httpRequest.getRequestDispatcher("/index.html").forward(httpRequest, httpResponse);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 초기화 필요 시 작성
    }

    @Override
    public void destroy() {
        // 자원 해제 필요 시 작성
    }
}
