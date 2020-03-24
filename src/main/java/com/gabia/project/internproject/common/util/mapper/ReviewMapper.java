package com.gabia.project.internproject.common.util.mapper;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.Review;
import com.gabia.project.internproject.controller.review.requestDto.ReviewPostDto;
import com.gabia.project.internproject.remoteService.googlePlaceService.dao.GoogleReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mappings({
            @Mapping(source = "rating", target = "rating", qualifiedByName = "defaultRatingValue"),
            @Mapping(source = "restaurantId", target = "restaurant", qualifiedByName = "pkToRestaurantEntity"),
            @Mapping(source = "memberId", target = "member", qualifiedByName = "pkToMemberEntity"),
            @Mapping(target="dateTime", ignore = true)
    })
    Review toEntity(ReviewPostDto reviewPostDto);

    @Named("defaultRatingValue")
    static int defaultRatingValue(Integer id) {
        if(id == null) {
            return 1;
        }
        return id;
    }

    @Named("pkToRestaurantEntity")
    static Restaurant pkToEntity(Integer id) {
        return Restaurant.of(id);
    }

    @Named("pkToMemberEntity")
    static Member pkToReviewEntity(Integer id) {
        return Member.of(id);
    }


    @Mapping(source = "text", target = "comment")
    @Mapping(source = "time", target = "dateTime", qualifiedByName = "stringToDateTime")
    @Mapping(target="restaurant", ignore = true)
    @Mapping(target="member", ignore = true)
    Review toEntity(GoogleReview googleReview);

    @Named("stringToDateTime")
    public static LocalDateTime pkToReviewEntity(int second) {
        if(second <= 0) {
            return LocalDateTime.now();
        }
        return LocalDateTime.of(1970, 1, 1, 0,0,1).plusSeconds(second);
    }
}
