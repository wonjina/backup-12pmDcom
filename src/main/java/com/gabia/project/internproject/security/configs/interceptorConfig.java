package com.gabia.project.internproject.security.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class interceptorConfig implements WebMvcConfigurer {

    @Value("${custom.path.upload-images}")
    private String uploadTargetUri;
    @Value("${custom.path.upload.source}")
    private String uploadSourceUri;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(uploadSourceUri)
                .addResourceLocations("file:///" + uploadTargetUri)
                .setCacheControl(CacheControl.maxAge(5, TimeUnit.MINUTES))
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
