package com.gabia.project.internproject.common.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int id;

    @CreatedDate
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    private String comment;

    @NotNull
    @Min(1)
    private int rating;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Review(String comment, int rating, Member member, Restaurant restaurant, LocalDateTime dateTime) {
        this.comment = comment;
        this.rating = rating;
        this.restaurant = restaurant;
        this.member = member;
        this.dateTime = dateTime;
    }

    /**
     * 연관관계 메소드
     */
    public void initRestaurant(Restaurant restaurant) {
        if(this.restaurant != null) {
            this.restaurant.getReviews().remove(this);
        }

        this.restaurant = restaurant;

        if(restaurant != null && !restaurant.getReviews().contains(this)) {
            this.restaurant.getReviews().add(this);
        }
    }
}
