package com.gabia.project.internproject.remoteService.googlePlaceService.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class Photos {
    private int height;
    private List<String> html_attributions;
    private String photo_reference;
    private int width;
}
