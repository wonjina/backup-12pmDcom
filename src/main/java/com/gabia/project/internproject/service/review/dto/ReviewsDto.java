package com.gabia.project.internproject.service.review.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabia.project.internproject.common.domain.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewsDto {

    @JsonProperty(value = "review_id")
    int id;

    @JsonProperty(value = "date")
    String dateTime;

    @JsonProperty(value = "comment")
    String comment;

    @JsonProperty(value = "rating")
    double rating;

    @JsonProperty(value = "writer")
    String writer;

    @JsonProperty(value = "writer_id")
    int writerId;

    @JsonProperty(value = "department")
    String department;

    public ReviewsDto(Review review) {
        this.id = review.getId();
        if(review.getDateTime() != null) {
            this.dateTime = review.getDateTime().toString();
        } else {
            this.dateTime = LocalDateTime.now().toString();
        }
        this.comment = review.getComment();
        this.rating = review.getRating();
        this.writer = review.getMember().getName();
        this.writerId = review.getMember().getId();
        this.department = review.getMember().getDepartment();
    }
}
