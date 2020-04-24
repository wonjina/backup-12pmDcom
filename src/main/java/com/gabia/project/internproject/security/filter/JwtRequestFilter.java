package com.gabia.project.internproject.security.filter;

import com.gabia.project.internproject.common.exception.UnknownUserException;
import com.gabia.project.internproject.common.helper.Notification;
import com.gabia.project.internproject.common.util.CustomCookies;
import com.gabia.project.internproject.security.properties.JwtCookieProperties;
import com.gabia.project.internproject.security.util.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomCookies customCookies;
    private final JwtCookieProperties jwtCookieProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, UnknownUserException, IOException, JwtException, IllegalArgumentException {

        Cookie cookie = WebUtils.getCookie(request, jwtCookieProperties.getName());
        System.out.println("filter cookie: "+cookie);
        if(!request.getMethod().toUpperCase().equals(HttpMethod.GET.toString().toUpperCase())) {
            if (cookie == null) {
                setErrorResponse(HttpStatus.UNAUTHORIZED, response);
                return;
            }

            try {
                jwtTokenProvider.getJwtValue(cookie.getValue());
            } catch (Exception e) {
                response.addCookie(customCookies.clearCookie(cookie));
                setErrorResponse(HttpStatus.UNAUTHORIZED, response);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }


    public void setErrorResponse(HttpStatus status, HttpServletResponse response) throws IOException {
        response.sendError(status.value());
        response.setStatus(status.value());
        response.setContentType("application/json");
    }
}
