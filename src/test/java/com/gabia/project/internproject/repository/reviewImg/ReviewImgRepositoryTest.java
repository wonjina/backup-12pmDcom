package com.gabia.project.internproject.repository.reviewImg;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.Review;
import com.gabia.project.internproject.common.domain.ReviewImg;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.repository.ReviewImgRepository;
import com.gabia.project.internproject.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReviewImgRepositoryTest {

    @Autowired
    ReviewImgRepository reviewImgRepository;
    @Autowired ReviewRepository reviewRepository;
    @Autowired RestaurantRepository restaurantRepository;
    @Autowired MemberRepository memberRepository;
    private Long beforeCount;

    @Test
    public void basicCRUD() {
        beforeCount = reviewImgRepository.count();
        Restaurant restaurant = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
                .reviewAmount(1L).rating(1)
                .zipCode("12345")
                .build();
        restaurantRepository.save(restaurant);

        Member member1 = Member.builder()
                .name("김인턴")
                .department("개발")
                .employeeNumber("GW12344")
                .build();
        Member member2 = Member.builder()
                .name("박인턴")
                .department("기획")
                .employeeNumber("GM12345")
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        Review review1 = Review.builder()
                .comment("맛있어요 사람이 너무 많았어요")
                .member(member1)
                .rating(4)
                .restaurant(restaurant)
                .build();
        Review review2 = Review.builder()
                .comment("맛있으나 서비스가 불친절함")
                .member(member2)
                .rating(2)
                .restaurant(restaurant)
                .build();
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        ReviewImg reviewImg1 = ReviewImg.builder()
                .url("/reviewImg1")
                .review(review1)
                .build();
        ReviewImg reviewImg2 = ReviewImg.builder()
                .url("/reviewImg2")
                .review(review2)
                .build();
        reviewImgRepository.save(reviewImg1);
        reviewImgRepository.save(reviewImg2);

        //단건 조회 검증
        ReviewImg findReviewImg1 = reviewImgRepository.findById(reviewImg1.getId()).get();
        ReviewImg findReviewImg2 = reviewImgRepository.findById(reviewImg2.getId()).get();
        assertThat(findReviewImg1).isEqualTo(reviewImg1);
        assertThat(findReviewImg2).isEqualTo(reviewImg2);

        //리스트 조회 검증
        List<ReviewImg> all = reviewImgRepository.findAll();
        assertThat(all.size()).isEqualTo(beforeCount+2);

        //카운트 검증
        long count = reviewImgRepository.count();
        assertThat(count).isEqualTo(beforeCount+2);

        //삭제 검증
        reviewImgRepository.delete(reviewImg1);
        reviewImgRepository.delete(reviewImg2);
        long deletedCount = reviewImgRepository.count();
        assertThat(deletedCount).isEqualTo(beforeCount);
    }

}