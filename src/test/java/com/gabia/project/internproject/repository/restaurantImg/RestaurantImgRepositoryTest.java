package com.gabia.project.internproject.repository.restaurantImg;

import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.RestaurantImg;
import com.gabia.project.internproject.repository.RestaurantImgRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RestaurantImgRepositoryTest {

    @Autowired RestaurantRepository restaurantRepository;
    @Autowired
    RestaurantImgRepository restaurantImgRepository;
    private Long beforeCount;

    @Test
    public void basicCRUD() {
        beforeCount = restaurantImgRepository.count();
        Restaurant restaurant = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
                .zipCode("12345")
                .build();
        restaurantRepository.save(restaurant);

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

        //단건 조회 검증
        RestaurantImg findRestaurantImg1 = restaurantImgRepository.findById(restaurantImg1.getId()).get();
        RestaurantImg findRestaurantImg2 = restaurantImgRepository.findById(restaurantImg2.getId()).get();
        assertThat(findRestaurantImg1).isEqualTo(restaurantImg1);
        assertThat(findRestaurantImg2).isEqualTo(restaurantImg2);

        //리스트 조회 검증
        List<RestaurantImg> all = restaurantImgRepository.findAll();
        assertThat(all.size()).isEqualTo(beforeCount+2);

        //카운트 검증
        long count = restaurantImgRepository.count();
        assertThat(count).isEqualTo(beforeCount+2);

        //삭제 검증
        restaurantImgRepository.delete(restaurantImg1);
        restaurantImgRepository.delete(restaurantImg2);
        long deletedCount = restaurantImgRepository.count();
        assertThat(deletedCount).isEqualTo(beforeCount);
    }

}