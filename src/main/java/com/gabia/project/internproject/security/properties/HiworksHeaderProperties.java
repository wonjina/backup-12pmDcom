package com.gabia.project.internproject.security.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
public class HiworksHeaderProperties {
    @Value("${spring.ouath.hiworks.header.typ}")
    private String typ;
    @Value("${spring.ouath.hiworks.header.alg}")
    private String alg;
}