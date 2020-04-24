package com.gabia.project.internproject.controller.review.requestDto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ReviewPostDto {
    String comment;
    Integer rating;
    Integer memberId;
    Integer restaurantId;

    public ReviewPostDto(String comment, int rating, int memberId, int restaurantId) {
        this.comment = comment;
        this.rating = rating;
        this.memberId = memberId;
        this.restaurantId = restaurantId;
    }

    public ReviewPostDto() {
    }
}
