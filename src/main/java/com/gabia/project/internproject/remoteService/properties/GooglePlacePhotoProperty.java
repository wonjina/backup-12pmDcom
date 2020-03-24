package com.gabia.project.internproject.remoteService.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@NoArgsConstructor
@Getter
public class GooglePlacePhotoProperty {

    @Value("${google.place.key}")
    private String key;
    @Value("${google.place.photo.api.height}")
    private String height;
    private String photoreference;

    public void initPhotoReference(String photoreference) {
        if(StringUtils.hasText(photoreference)) {
            this.photoreference = photoreference;
        }
    }
}