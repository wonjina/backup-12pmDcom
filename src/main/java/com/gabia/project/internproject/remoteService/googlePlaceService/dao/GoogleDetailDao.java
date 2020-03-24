package com.gabia.project.internproject.remoteService.googlePlaceService.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class GoogleDetailDao {
    private String formatted_address;
    private Geometry geometry;
    private String name;
    private String formatted_phone_number;
    private List<GoogleReview> reviews;
    private String url;
    private List<Photos> photos;
    private String website;
    private String place_id;

}
