package com.gabia.project.internproject.security.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
public class JwtCookieProperties {

    @Value("${spring.jwt.cookie.name}")
    private String name;

    @Value("${spring.jwt.cookie.expire}")
    private Integer expire;

    @Value("${spring.jwt.cookie.path}")
    private String path;
}