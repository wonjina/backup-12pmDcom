package com.gabia.project.internproject.controller.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ResFilterDto {

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "restaurant_id")
    private Integer id;

    @JsonProperty(value = "sort_field")
    private String sortField;

    @JsonProperty(value = "sort_by")
    private String sortBy;

}
