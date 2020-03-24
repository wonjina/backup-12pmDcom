package com.gabia.project.internproject.service.restaurant;

import com.gabia.project.internproject.common.helper.Notification;
import com.gabia.project.internproject.controller.restaurant.dto.ResFilterDto;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.service.restaurant.dto.CategoriesDto;
import com.gabia.project.internproject.service.restaurant.dto.RestaurantDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
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

    public Page<RestaurantDto> getRestaurantsList(ResFilterDto resFilterDto, Pageable pageable) {
        List<RestaurantDto> restaurants = restaurantRepository.findAll(where(fetchReview())
                                                            .and(equalsRestaurantName(resFilterDto.getName()))
                                                            .and(equalsRestaurantId(resFilterDto.getId()))
                                                    ).stream()
                                                    .filter(distinctByKey(r -> r.getId()))
                                                    .map( r -> new RestaurantDto(r))
                                                    .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), restaurants.size());

        if(resFilterDto.getSortField() != null && resFilterDto.getSortField().equals("review")) {
            this.sortByReviewAmountList(restaurants);
        } else /*if(resFilterDto.getSortField() != null && resFilterDto.getSortField().equals("rating"))*/{
            this.sortByRatingList(restaurants);
        }

        return new PageImpl(  restaurants.subList(start, end)
                , pageable
                , restaurants.size());
    }


    public RestaurantDto getRestaurantDetail(Integer id) {
        return restaurantRepository.findOne(where(fetchReview())
                                            .and(equalsRestaurantId(id)))
                            .map(RestaurantDto::new)
                            .orElseThrow(() ->
                                    new HttpClientErrorException(HttpStatus.NOT_FOUND, Notification.NOT_FOUND.getMsg()));
    }

    /**
     *  Stream 시 중복체크
     */
    private <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    private void sortByRatingList(List<RestaurantDto> restaurants) {
        Collections.sort(restaurants, new Comparator<RestaurantDto>() {
            @Override
            public int compare(RestaurantDto res1, RestaurantDto res2) {
                if(res2.getRating() > res1.getRating()) {
                    return 1;
                } else if(res2.getRating() == res1.getRating()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }

    private void sortByReviewAmountList(List<RestaurantDto> restaurants) {
        Collections.sort(restaurants, new Comparator<RestaurantDto>() {
            @Override
            public int compare(RestaurantDto res1, RestaurantDto res2) {
                if(res2.getReviewsAmount() > res1.getReviewsAmount()) {
                    return 1;
                } else if(res2.getReviewsAmount() == res1.getReviewsAmount()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });
    }

}