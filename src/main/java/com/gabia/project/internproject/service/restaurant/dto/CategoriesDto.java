package com.gabia.project.internproject.service.restaurant.dto;

import lombok.Getter;
import java.util.Set;

@Getter
public class CategoriesDto {

    private Set<String> categories;

    public CategoriesDto(Set<String> categories) {
        this.categories = categories;
    }
}