package com.rishabh.smartexpensetracker.interceptor;

import com.rishabh.smartexpensetracker.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RateLimitService rateLimitService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        String user = request.getUserPrincipal().getName();

        if (!rateLimitService.isAllowed(user)) {

            response.setStatus(429);
            response.getWriter().write("Too many requests");

            return false;
        }

        return true;
    }
}
