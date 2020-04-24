package com.gabia.project.internproject.service.review;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.Review;
import com.gabia.project.internproject.common.helper.Notification;
import com.gabia.project.internproject.common.util.mapper.ReviewMapper;
import com.gabia.project.internproject.controller.review.requestDto.ReviewFilterDto;
import com.gabia.project.internproject.controller.review.requestDto.ReviewPostDto;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.repository.ReviewRepository;
import com.gabia.project.internproject.service.review.dto.ReviewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

import static com.gabia.project.internproject.common.helper.customSpecifications.ReviewSpecifications.equalsRating;
import static com.gabia.project.internproject.common.helper.customSpecifications.ReviewSpecifications.equalsRestaurant;
import static com.gabia.project.internproject.common.helper.customSpecifications.ReviewSpecifications.equalsReviewId;
import static com.gabia.project.internproject.common.helper.customSpecifications.ReviewSpecifications.equalsWriter;
import static com.gabia.project.internproject.common.helper.customSpecifications.ReviewSpecifications.fetchMember;
import static com.gabia.project.internproject.common.helper.customSpecifications.ReviewSpecifications.gtDateTime;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final RestaurantRepository restaurantRepository;

    /**
     * 리뷰 리스트 조회
     * @param reviewFilterDto : 날짜, 작성자, 평점, 가게id, 리뷰id
     * @param pageable : 정렬 및 페이징
     * @return :
     */
    public Page<ReviewsDto> getReviewsList(ReviewFilterDto reviewFilterDto, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAll(where(fetchMember())
                                                                .and(gtDateTime(reviewFilterDto.getDateTime()))
                                                                .and(equalsWriter(Member.of(reviewFilterDto.getMemberId())))
                                                                .and(equalsRating(reviewFilterDto.getRating()))
                                                                .and(equalsRestaurant(Restaurant.of(reviewFilterDto.getRestaurantId())))
                                                                .and(equalsReviewId(reviewFilterDto.getId())),
                                                                pageable);

        //hatoxes 로 변환하기 위해 page list 형태로 리턴
        return new PageImpl(  reviews.stream().map(ReviewsDto::new).collect(Collectors.toList())
                                , pageable
                                , reviews.getTotalPages() * reviews.getSize());

    }

    /**
     * 리뷰 쓰기 (단건)
     */
    @Transactional
    public String writeReview(ReviewPostDto reviewPostDto) {
        Restaurant restaurant = restaurantRepository.findRestaurantWithReviewsById(reviewPostDto.getRestaurantId());
        restaurant.addReview(reviewMapper.toEntity(reviewPostDto));
        restaurant.updateReviewsAmount();
        restaurant.updateRating();
        return Notification.SUCCESS_POST.getMsg();
    }

}
