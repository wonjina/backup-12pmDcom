package com.gabia.project.internproject.repository.review;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.Review;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.repository.ReviewRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MemberRepository memberRepository;

    private Long beforeCount;

    @BeforeEach
    public void setUp() {
        beforeCount = reviewRepository.count();
    }

    @AfterEach
    public void cleanUp() { }

     @Test
    public void 리뷰쓰기() {
        Member member = Member.builder().department("test부서").employeeNumber("testnumber").name("test name").build();
        Restaurant restaurant = Restaurant.builder().name("test 가게이름").cellNumber("1245781")
                .loadAddress("테스트 주소").build();
        memberRepository.save(member);
        restaurantRepository.save(restaurant);

        List<Review> reviewsList = new ArrayList<>();
        Review review = Review.builder().comment("test 리뷰 남기기").rating(1).member(member).restaurant(restaurant).build();

        reviewsList.add(review);

        Review saveReview = reviewRepository.save(review);
        assertThat(beforeCount+reviewsList.size()).isEqualTo(reviewRepository.count());
        assertThat(saveReview.getComment()).isEqualTo(review.getComment());
        assertThat(saveReview.getRating()).isEqualTo(review.getRating());
        assertThat(saveReview.getMember()).isEqualTo(review.getMember());
        assertThat(saveReview.getRestaurant()).isEqualTo(review.getRestaurant());
    }

    @Test
    public void basicCRUD() {
        beforeCount = reviewRepository.count();

        Restaurant restaurant = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
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

        // 단건 조회 검증
        Review findReview1 = reviewRepository.findById(review1.getId()).get();
        Review findReview2 = reviewRepository.findById(review2.getId()).get();
        assertThat(findReview1).isEqualTo(review1);
        assertThat(findReview2).isEqualTo(review2);

        // 리스트 조회 검증
        List<Review> all = reviewRepository.findAll();
        assertThat(all.size()).isEqualTo(beforeCount+2);

        // 카운트 검증
        long count = reviewRepository.count();
        assertThat(count).isEqualTo(beforeCount+2);

        // 삭제 검증
        reviewRepository.delete(review1);
        reviewRepository.delete(review2);
        long deletedCount = reviewRepository.count();
        assertThat(deletedCount).isEqualTo(beforeCount);
    }

}