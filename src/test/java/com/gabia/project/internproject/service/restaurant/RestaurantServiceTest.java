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
    RestaurantImgRepository restaurantImgRepository;

    @Autowired
    EntityManager em;
    Pageable pageable;
    @Test
    public void getResList() {

        Restaurant sampleRestaurant = Restaurant.builder()
                .name("중국집")
                .category("중식")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
                .zipCode("12345")
                .build();
        sampleRestaurant = restaurantRepository.save(sampleRestaurant);

        pageable = PageRequest.of(0, 5);

        ResFilterDto resFilterDto =new ResFilterDto();
        resFilterDto.setId(sampleRestaurant.getId());

        List<RestaurantDto> restaurants = restaurantService.getRestaurantsList(resFilterDto, pageable).getContent();
        assertThat(1).isEqualTo(restaurants.size());
        assertThat(restaurants.get(0).getRestaurantId()).isEqualTo(sampleRestaurant.getId());

    }

}
