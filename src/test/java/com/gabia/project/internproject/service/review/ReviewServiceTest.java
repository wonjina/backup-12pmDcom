package com.gabia.project.internproject.service.review;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.Review;
import com.gabia.project.internproject.controller.review.requestDto.ReviewFilterDto;
import com.gabia.project.internproject.controller.review.requestDto.ReviewPostDto;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.repository.ReviewRepository;
import com.gabia.project.internproject.service.review.dto.ReviewsDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.gabia.project.internproject.common.helper.customSpecifications.ReviewSpecifications.*;
import static com.gabia.project.internproject.common.helper.customSpecifications.ReviewSpecifications.equalsReviewId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;

@SpringBootTest
@Transactional
class ReviewServiceTest {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    EntityManager em;
    Pageable pageable = PageRequest.of(0, 5);
    @Test
    public void 리뷰작성() {
        Restaurant restaurant = Restaurant.builder()
                .name("중국집Test")
                .cellNumber("11")
                .loadAddress("4444")
                .locationX(3)
                .locationY(4)
                .zipCode("112")
                .build();
        Member member = Member.builder().name("test employee").build();
        Member createdMember = memberRepository.save(member);
        Restaurant createdRestataurant = restaurantRepository.save(restaurant);

        //리뷰 작성
        ReviewPostDto reviewPostDto = new ReviewPostDto("test commment", 1, createdMember.getId(), createdRestataurant.getId());
        int result = reviewService.writeReview(reviewPostDto);

        //작성한 리뷰 검색
        assertThat(result).isEqualTo(reviewRepository.findById(result).get().getId());

    }

    @Test
    public void 리뷰조회() {
        Restaurant restaurant = Restaurant.builder()
                .name("중국집Test")
                .cellNumber("11")
                .loadAddress("4444")
                .locationX(3)
                .locationY(4)
                .zipCode("112")
                .rating(0)
                .reviewAmount(0l)
                .build();
        Member member = Member.builder().name("test employee").build();
        Member createdMember = memberRepository.save(member);
        Restaurant createdRestataurant = restaurantRepository.save(restaurant);

        List<Integer> ids = new ArrayList<>();

        //리뷰 저장
        ReviewPostDto reviewPostDto = new ReviewPostDto("test commment", 1, createdMember.getId(), createdRestataurant.getId());
        ids.add(reviewService.writeReview(reviewPostDto));
        reviewPostDto = new ReviewPostDto("test commment", 1, createdMember.getId(), createdRestataurant.getId());
        ids.add(reviewService.writeReview(reviewPostDto));

        //검색 필터 조건 생성
        ReviewFilterDto reviewFilterDto = new ReviewFilterDto(null, null, null,null,createdRestataurant.getId(), null);

        //리뷰 검색
        Page<ReviewsDto> list = reviewService.getReviewsList(reviewFilterDto, pageable);
        assertThat(list.getContent().size()).isGreaterThanOrEqualTo(ids.size());

    }
}