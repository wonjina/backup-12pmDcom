package com.gabia.project.internproject.repository.menu;

import com.gabia.project.internproject.common.domain.Menu;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.repository.MenuRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MenuRepositoryTest {

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    private Long beforeCount;

    @Test
    public void basicCRUD() {
        beforeCount = menuRepository.count();
        Restaurant restaurant = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
                .zipCode("12345")
                .build();
        restaurantRepository.save(restaurant);

        Menu menu1 = Menu.builder()
                .name("자장면")
                .price(5500)
                .restaurant(restaurant)
                .build();
        Menu menu2 = Menu.builder()
                .name("짬뽕")
                .price(6000)
                .restaurant(restaurant)
                .build();
        menuRepository.save(menu1);
        menuRepository.save(menu2);

        //단건 조회 검증
        Menu findMenu1 = menuRepository.findById(menu1.getId()).get();
        Menu findMenu2 = menuRepository.findById(menu2.getId()).get();
        assertThat(findMenu1).isEqualTo(menu1);
        assertThat(findMenu2).isEqualTo(menu2);

        //리스트 조회 검증
        List<Menu> all = menuRepository.findAll();
        assertThat(all.size()).isEqualTo(beforeCount+2);

        //카운트 검증
        long count = menuRepository.count();
        assertThat(count).isEqualTo(beforeCount+2);

        //삭제 검증
        menuRepository.delete(menu1);
        menuRepository.delete(menu2);
        long deletedCount = menuRepository.count();
        assertThat(deletedCount).isEqualTo(beforeCount);
    }

}