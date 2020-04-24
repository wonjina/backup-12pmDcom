package com.gabia.project.internproject.service.restaurantImg;

import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.RestaurantImg;
import com.gabia.project.internproject.controller.restaurantImg.dto.ResImgFilterDto;
import com.gabia.project.internproject.repository.RestaurantImgRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.service.restaurantImg.dto.RestaurantImgDto;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RestaurantImgServiceTest {

    @Autowired RestaurantImgService restaurantImgService;
    @Autowired RestaurantImgRepository restaurantImgRepository;
    @Autowired RestaurantRepository restaurantRepository;

    @Test
    public void getResImg() {

        Restaurant restaurant = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
                .zipCode("12345")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        RestaurantImg restaurantImg1 = RestaurantImg.builder()
                .restaurant(restaurant)
                .url("/testImg")
                .build();
        RestaurantImg restaurantImg2 = RestaurantImg.builder()
                .restaurant(restaurant)
                .url("/testImgg")
                .build();
        restaurantImgRepository.save(restaurantImg1);
        restaurantImgRepository.save(restaurantImg2);

        List<RestaurantImg> restaurantImgList = new LinkedList<>();
        restaurantImgList.add(restaurantImg1);
        restaurantImgList.add(restaurantImg2);

        ResImgFilterDto resImgFilterDto = new ResImgFilterDto();
        Pageable pageable = PageRequest.of(0,5);

        List<RestaurantImgDto> list = restaurantImgService.getRestaurantImgList(resImgFilterDto, pageable);
    }

}
