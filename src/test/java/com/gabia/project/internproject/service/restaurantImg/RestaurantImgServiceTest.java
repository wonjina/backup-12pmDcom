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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RestaurantImgServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantImgRepository restaurantImgRepository;

    @Autowired
    RestaurantImgService restaurantImgService;

    private Restaurant restaurant1;
    private RestaurantImg restaurantImg1, restaurantImg2, restaurantImg3;
    private long beforeRICount;

    Pageable pageable = PageRequest.of(0, 5);

    @BeforeEach
    public void setUp() {
        // 음식점
        restaurant1 = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
                .zipCode("12345")
                .build();

        // 음식점 이미지
        restaurantImg1 = RestaurantImg.builder()
                .restaurant(restaurant1)
                .url("https://img.com/abcd")
                .build();
        restaurantImg2 = RestaurantImg.builder()
                .restaurant(restaurant1)
                .url("https://img.com/aaaa")
                .build();
        restaurantImg3 = RestaurantImg.builder()
                .restaurant(restaurant1)
                .url("https://img.com/bbbb")
                .build();
    }

    @Test
    public void 음식점_이미지_findAll_테스트() {
        beforeRICount = restaurantImgRepository.count();

        // save
        restaurantRepository.save(restaurant1);
        restaurantImgRepository.save(restaurantImg1);
        restaurantImgRepository.save(restaurantImg2);
        restaurantImgRepository.save(restaurantImg3);

        // 리스트 조회 검증
        List<RestaurantImg> imgs = restaurantImgRepository.findAll();
        assertThat(imgs.size()).isEqualTo(beforeRICount+3);
    }

    @Test
    public void 음식점_Detail_이미지_조회_테스트() {
        List<RestaurantImg> restaurantImgs = new ArrayList<>();
        // save
        restaurantRepository.save(restaurant1);
        restaurantImgs.add(restaurantImgRepository.save(restaurantImg1));
        restaurantImgs.add(restaurantImgRepository.save(restaurantImg2));
        restaurantImgs.add(restaurantImgRepository.save(restaurantImg3));

        Page<RestaurantImgDto> restaurantImg = restaurantImgService.getRestaurantImgDetail(restaurant1.getId(), pageable);
        assertThat(restaurantImg.getContent().size()).isEqualTo(Math.min(pageable.getPageSize(), restaurantImgs.size()));

    }

    @Test
    public void 음식점_이미지_조회_테스트() {
        List<RestaurantImg> dbResImgs = restaurantImgRepository.findAll();
        if(dbResImgs.size() >0) {
            List<RestaurantImg> restaurantImgs = new ArrayList<>();
            // save
            restaurantRepository.save(restaurant1);
            restaurantImgs.add(restaurantImgRepository.save(restaurantImg1));
            restaurantImgs.add(restaurantImgRepository.save(restaurantImg2));
            restaurantImgs.add(restaurantImgRepository.save(restaurantImg3));

            ResImgFilterDto resImgFilterDto = new ResImgFilterDto();
            resImgFilterDto.setSortField("rating");

            Page<RestaurantImgDto> restaurantImg = restaurantImgService.getRestaurantImgList(resImgFilterDto, pageable);
            assertThat(restaurantImg.getContent().size()).isGreaterThan(0);
        }
    }

}
