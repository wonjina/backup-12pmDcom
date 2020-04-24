package com.gabia.project.internproject.remoteService.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@NoArgsConstructor
@Getter
public class GooglePlaceDetailProperty {
    @Value("${google.place.key}")
    private String key;
    @Value("${google.place.language}")
    private String language;
    private String place_id;

    public void initPlaceId(String placeId) {
        if(StringUtils.hasText(placeId)) {
            this.place_id = placeId;
        }
    }

}