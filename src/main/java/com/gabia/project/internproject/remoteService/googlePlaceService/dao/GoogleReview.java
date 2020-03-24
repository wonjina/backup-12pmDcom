package com.gabia.project.internproject.remoteService.googlePlaceService.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;


@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GoogleReview {
    private String author_name;
    private String author_url;
    private String profile_photo_url;
    private int rating;
    private String relative_time_description;
    private String text;
    private int time;

    public GoogleReview() {}
}
