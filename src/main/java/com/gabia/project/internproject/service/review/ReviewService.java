package com.gabia.project.internproject.service.review;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.Review;
import com.gabia.project.internproject.common.util.mapper.ReviewMapper;
import com.gabia.project.internproject.controller.review.requestDto.ReviewFilterDto;
import com.gabia.project.internproject.controller.review.requestDto.ReviewPostDto;
import com.gabia.project.internproject.repository.ReviewRepository;
import com.gabia.project.internproject.service.restaurant.dto.RestaurantDto;
import com.gabia.project.internproject.service.review.dto.ReviewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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

    /**
     * 리뷰 리스트 조회
     * @param reviewFilterDto
     * @return
     */
    public Page<ReviewsDto> getReviewsList(ReviewFilterDto reviewFilterDto, Pageable pageable) {
        List<ReviewsDto> reviewsDtos = reviewRepository.findAll(where(fetchMember())
                                                                .and(gtDateTime(reviewFilterDto.getDateTime()))
                                                                .and(equalsWriter(Member.of(reviewFilterDto.getMemberId())))
                                                                .and(equalsRating(reviewFilterDto.getRating()))
                                                                .and(equalsRestaurant(Restaurant.of(reviewFilterDto.getRestaurantId())))
                                                                .and(equalsReviewId(reviewFilterDto.getId())))
                                                    .stream()
                                                    .map(ReviewsDto::new)
                                                    .collect(Collectors.toList());

        // 날짜기준 내림차순 정렬
        this.sortByDateTimeList(reviewsDtos);

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), reviewsDtos.size());

        return new PageImpl(  reviewsDtos.subList(start,end)
                                , pageable
                                , reviewsDtos.size());
    }

    /**
     * 리뷰 쓰기 (단건)
     */
    @Transactional
    public int writeReview(ReviewPostDto reviewPostDto) {
        return reviewRepository.save(reviewMapper.toEntity(reviewPostDto)).getId();
    }

    private void sortByDateTimeList(List<ReviewsDto> restaurants) {
        Collections.sort(restaurants, new Comparator<ReviewsDto>() {
            @Override
            public int compare(ReviewsDto rev1, ReviewsDto rev2) {
                LocalDateTime firtsTime = LocalDateTime.parse(rev1.getDateTime());
                LocalDateTime secondTime = LocalDateTime.parse(rev2.getDateTime());

                if(firtsTime.isAfter(secondTime)) {
                    return -1;
                } else if(firtsTime.isBefore(secondTime)) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

}
