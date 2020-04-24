package com.gabia.project.internproject.remoteService.googlePlaceService.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Geometry {
    private GoogleLocation location;
}
