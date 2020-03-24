package com.gabia.project.internproject.repository.restaurant;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.Review;
import com.gabia.project.internproject.common.helper.customSpecifications.RestaurantSpecifications;
import com.gabia.project.internproject.controller.restaurant.dto.ResFilterDto;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;

@SpringBootTest
@Transactional
class RestaurantRepositoryTest {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MemberRepository memberRepository;

    private Long beforeCount;

    @Autowired
    EntityManager em;


    @Test
    public void specification테스트() {
        Specification<Restaurant> spec = where(null);
        ResFilterDto resInfoReqDto = new ResFilterDto();

        List<Restaurant> list2 = restaurantRepository.findAll(where(RestaurantSpecifications.equalsRestaurantName(resInfoReqDto.getName()))
                                                                        .and(RestaurantSpecifications.equalsRestaurantId(resInfoReqDto.getId())));
        assertThat(list2.size()).isEqualTo(restaurantRepository.count());


        System.out.println(list2.size());
    }

    @Test
    public void 양방향엔티티_basicCRUD() {
        /**
         * 삽입 Test
         */
        long beforeResCount = restaurantRepository.count();
        long beforeReviewCount = reviewRepository.count();

        Member member = Member.builder()
                            .name("test Emp name")
                            .employeeNumber("test EmpNum")
                            .department("test department")
                            .build();
        Member saveMember = memberRepository.save(member);
        em.flush();

        Restaurant restaurant1 = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
                .zipCode("12345")
                .build();

        List<Review> reviewList = new ArrayList<>();

        // 리뷰 생성 및 restaurant 에 추가
        IntStream.rangeClosed(0,4).forEach(i -> {
            Review review = Review.builder()
                    .comment("맛없습니다. 진심으로.. "+i)
                    .rating(2)
                    .member(saveMember)
                    .build();
            review.setDateTime(LocalDateTime.now());
            reviewList.add(review);
            restaurant1.addReview(review);
        });

        restaurantRepository.save(restaurant1);
        em.flush();

        // review 저장여부 검사
        reviewList.stream().forEach( rev -> {
            assertThat(rev.getId()).isEqualTo(reviewRepository.findById(rev.getId()).get().getId());
        });

        // restaurant 저장여부 검사
        assertThat(restaurant1).isEqualTo(restaurantRepository.findById(restaurant1.getId()).get());

        assertThat(restaurant1.getReviews().size()).isEqualTo(reviewList.size());

        /**
         * 삭제 Test
         */
        assertThat(beforeResCount).isNotEqualTo(restaurantRepository.count());
        assertThat(beforeReviewCount).isNotEqualTo(reviewRepository.count());

        em.remove(restaurant1);
        em.flush();

        assertThat(beforeResCount).isEqualTo(restaurantRepository.count());
        assertThat(beforeReviewCount).isEqualTo(reviewRepository.count());
    }

    @Test
    public void basicCRUD() {
        beforeCount = restaurantRepository.count();

        Restaurant restaurant1 = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
                .zipCode("12345")
                .build();
        Restaurant restaurant2 = Restaurant.builder()
                .name("김밥천국")
                .cellNumber("029704567")
                .loadAddress("12312")
                .locationX(121)
                .locationY(100.3)
                .zipCode("45678")
                .build();
        restaurantRepository.save(restaurant1);
        restaurantRepository.save(restaurant2);

        // 단건 조회 검증
        Restaurant findRestaurant1 = restaurantRepository.findById(restaurant1.getId()).get();
        Restaurant findRestaurant2 = restaurantRepository.findById(restaurant2.getId()).get();
        assertThat(findRestaurant1).isEqualTo(restaurant1);
        assertThat(findRestaurant2).isEqualTo(restaurant2);

        // 리스트 조회 검증
        List<Restaurant> all = restaurantRepository.findAll();
        assertThat(all.size()).isEqualTo(beforeCount+2);

        // 카운트 검증
        long count = restaurantRepository.count();
        assertThat(count).isEqualTo(beforeCount+2);

        // 삭제 검증
        restaurantRepository.delete(restaurant1);
        restaurantRepository.delete(restaurant2);
        long deletedCount = restaurantRepository.count();
        assertThat(deletedCount).isEqualTo(beforeCount);
    }

}