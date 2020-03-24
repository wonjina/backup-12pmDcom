package com.gabia.project.internproject.service.restaurant;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.Review;
import com.gabia.project.internproject.controller.restaurant.dto.ResFilterDto;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.service.restaurant.dto.CategoriesDto;
import com.gabia.project.internproject.service.restaurant.dto.RestaurantDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
class RestaurantServiceTest {
    @Autowired
    RestaurantService restaurantService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 가게리스트가져오기() {
        List<RestaurantDto> list;
        ResFilterDto resFilterDto;
        Pageable pageable = PageRequest.of(0, 5);

        //음식점 저장
        Restaurant restaurant = Restaurant.builder()
                .name("중국집Test")
                .cellNumber("11")
                .loadAddress("4444")
                .locationX(3)
                .locationY(4)
                .zipCode("112")
                .build();
        Restaurant createdRestataurant = restaurantRepository.save(restaurant);

        //저장된 음식점 검색
        resFilterDto = new ResFilterDto();
        resFilterDto.setName("중국집Test");
        list = restaurantService.getRestaurantsList(resFilterDto, pageable).getContent();

        assertThat(list.size()).isGreaterThanOrEqualTo(1);

        //저장된 음식점 검색
        resFilterDto = new ResFilterDto();
        resFilterDto.setId(restaurant.getId());
        list = restaurantService.getRestaurantsList(resFilterDto, pageable).getContent();

        assertThat(list.get(0).getRestaurantId()).isEqualTo(createdRestataurant.getId());
    }

    @Test
    public void getDetailTest() {

        //음식점 저장
        Restaurant restaurant = Restaurant.builder()
                .name("중국집Test")
                .cellNumber("11")
                .loadAddress("4444")
                .locationX(3)
                .locationY(4)
                .zipCode("112")
                .build();
        Member member = Member.builder().name("test employee").build();
        memberRepository.save(member);

        Review rev1 = Review.builder()
                            .comment("test comment 1")
                            .rating(3)
                            .member(member)
                            .restaurant(restaurant)
                            .build();

        Review rev2 = Review.builder()
                            .comment("test comment 2")
                            .rating(4)
                            .restaurant(restaurant)
                            .member(member)
                            .build();

        //cacade 설정을 통해 review 도 저장
        restaurant.addReview(rev1);
        restaurant.addReview(rev2);

        List<Review> reviews = new ArrayList<>();
        reviews.add(rev1);
        reviews.add(rev2);

        Restaurant createdRestataurant = restaurantRepository.save(restaurant);


        RestaurantDto restaurantDto = restaurantService.getRestaurantDetail(createdRestataurant.getId());
        assertThat(restaurantDto.getName()).isEqualTo(restaurant.getName());
        assertThat(restaurantDto.getReviewsAmount()).isEqualTo(reviews.size());

    }

}