package com.gabia.project.internproject.service.restaurant;

import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.helper.Notification;
import com.gabia.project.internproject.controller.restaurant.dto.ResFilterDto;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.service.restaurant.dto.RestaurantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

import static com.gabia.project.internproject.common.helper.customSpecifications.RestaurantSpecifications.equalsRestaurantId;
import static com.gabia.project.internproject.common.helper.customSpecifications.RestaurantSpecifications.equalsRestaurantName;
import static com.gabia.project.internproject.common.helper.customSpecifications.RestaurantSpecifications.fetchReview;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    /**
     * 가게 리스트 정보
     * @param resFilterDto 가게이름 , 가게 id
     * @param pageable : 정렬 및 페이징
     * @return
     */
    public Page<RestaurantDto> getRestaurantsList(ResFilterDto resFilterDto, Pageable pageable) {
        List<RestaurantDto> restaurantDtos = restaurantRepository.findAll(where(equalsRestaurantName(resFilterDto.getName()))
                                                                                .and(equalsRestaurantId(resFilterDto.getId())),
                                                                            pageable
                                                                        ).getContent()
                                                                        .stream()
                                                                        .map(RestaurantDto::new)
                                                                        .collect(Collectors.toList());
        return new PageImpl(restaurantDtos
                            , pageable
                            , restaurantRepository.count());
    }

    /**
     * 가게 상세정보
     * @param id  : 가게 기본키
     * @return
     */
    public RestaurantDto getRestaurantDetail(Integer id) {
        return restaurantRepository.findOne(where(fetchReview())
                                            .and(equalsRestaurantId(id)))
                                    .map(RestaurantDto::new)
                                    .orElseThrow(() ->
                                            new HttpClientErrorException(HttpStatus.NOT_FOUND, Notification.NOT_FOUND.getMsg()));
    }

    /**
     * 가게별 리뷰 갯수, 평점 update
     */
    @Transactional
    public void updateReviewInfo() {
        List<Restaurant> restaurantList= restaurantRepository.findAll(where(fetchReview()));
        restaurantList.stream().peek(r -> {
                                        r.updateRating();
                                        r.updateReviewsAmount();
                                    });
    }

}