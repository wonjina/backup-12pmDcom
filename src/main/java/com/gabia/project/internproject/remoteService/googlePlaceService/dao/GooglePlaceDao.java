package com.gabia.project.internproject.remoteService.googlePlaceService.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class GooglePlaceDao {
    private Geometry geometry;
    private String icon;
    private String id;
    private String name;
    private List<Photos> photos;
    private String place_id;
    private String reference;
    private List<String> types;
    private String vicinity;
}
