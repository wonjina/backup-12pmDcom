package com.gabia.project.internproject.remoteService.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Getter
public class GooglePlaceParamsProperty {

    @Value("${google.place.location}")
    private String location;
    @Value("${google.place.radius}")
    private String radius;
    @Value("${google.place.type}")
    private String type;
    @Value("${google.place.key}")
    private String key;
    @Value("${google.place.language}")
    private String language;

}