package com.gabia.project.internproject.common.util;


import com.gabia.project.internproject.security.properties.JwtCookieProperties;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;

@Component
public class CustomCookies {

    @Value("${spring.server.ip}")
    private String serverIp;

    public Cookie createCookie(JwtCookieProperties jwtCookieProperties, String jwtToken) {
        Cookie cookie = new Cookie(jwtCookieProperties.getName() , jwtToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(jwtCookieProperties.getExpire());
        cookie.setPath(jwtCookieProperties.getPath());
        return cookie;
    }

    public Cookie clearCookie(Cookie cookie) {
        cookie.setMaxAge(0);
        cookie.setPath(cookie.getPath());
        return cookie;
    }
}
