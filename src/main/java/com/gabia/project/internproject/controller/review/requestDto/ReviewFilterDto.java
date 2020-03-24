package com.gabia.project.internproject.controller.review.requestDto;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Review List 검색 Request Dto
 */
@Getter
@Setter
@NoArgsConstructor
public class ReviewFilterDto {

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Integer rating;
    private Integer id;
    private Integer restaurantId;
    private Integer memberId;
    private Member member;
    private Restaurant restaurant;

    public ReviewFilterDto(LocalDateTime dateTime,
                           LocalDate date,
                           Integer memberId,
                           Integer rating,
                           Integer restaurantId,
                           Integer reviewId) {
        this.dateTime = dateTime;
        this.date = date;
        this.member = Member.of(memberId);
        this.rating = rating;
        this.restaurantId = restaurantId;
        this.restaurant = Restaurant.of(restaurantId);
        this.id = reviewId;
    }

    public LocalDateTime convertDateToDateTime() {
        return dateTime = LocalDateTime.of(this.date, LocalTime.of(0,0,1));
    }

}
