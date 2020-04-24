package com.gabia.project.internproject.security.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
public class HiworksProperties {
    @Value("${spring.ouath.hiworks.clientId}")
    private String client_id;
    @Value("${spring.ouath.hiworks.clientSecret}")
    private String client_secret;
    @Value("${spring.ouath.hiworks.grant_type}")
    private String grant_type;
    @Value("${spring.ouath.hiworks.access_type}")
    private String access_type;

    private String auth_code;

    public void initAuthCode(String authCode) {
        this.auth_code = authCode;
    }
}
