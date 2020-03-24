package com.gabia.project.internproject.service.restaurantImg;

import com.gabia.project.internproject.common.exception.BusinessException;
import com.gabia.project.internproject.controller.restaurant.dto.ResFilterDto;
import com.gabia.project.internproject.controller.restaurantImg.dto.ResImgFilterDto;
import com.gabia.project.internproject.repository.RestaurantImgRepository;
import com.gabia.project.internproject.service.restaurant.RestaurantService;
import com.gabia.project.internproject.service.restaurant.dto.RestaurantDto;
import com.gabia.project.internproject.service.restaurantImg.dto.RestaurantImgDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static com.gabia.project.internproject.common.helper.customSpecifications.RestaurantImgSpecification.equalRestaurant;
import static org.springframework.data.jpa.domain.Specification.where;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class RestaurantImgService {

    private final RestaurantImgRepository restaurantImgRepository;
    private final RestaurantService restaurantService;

    @Value("${spring.server.domain}")
    private String DOMAIN_URL;

    // 음식점 이미지 조회
    public Page<RestaurantImgDto> getRestaurantImgDetail(Integer id, Pageable pageable) throws BusinessException {
        List<RestaurantImgDto> restaurantImgs = restaurantImgRepository.findAll(where(equalRestaurant(id)))
                .stream()
                .map(r -> new RestaurantImgDto(r, DOMAIN_URL))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), restaurantImgs.size());

        return new PageImpl(restaurantImgs.subList(start,end), pageable, restaurantImgs.size());
    }

    public Page<RestaurantImgDto> getRestaurantImgList(ResImgFilterDto resImgFilterDto, Pageable pageable) throws BusinessException {
        ResFilterDto resFilterDto = new ResFilterDto();
        resFilterDto.setSortField(resImgFilterDto.getSortField());

        //get 식당 리스트
        List<RestaurantDto> list = restaurantService.getRestaurantsList(resFilterDto, pageable).getContent();

        //식당 id list 추출
        List<Integer> resIds = list.stream().map(RestaurantDto::getRestaurantId).collect(Collectors.toList());

        //식당 ids 로 식당 이미지 get
        List<RestaurantImgDto> restaurantImgs = restaurantImgRepository.findAll(where(equalRestaurant(resIds))).stream()
                .filter(distinctByKey(resImg ->resImg.getRestaurant().getId()))
                .map(r -> new RestaurantImgDto(r, DOMAIN_URL))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), restaurantImgs.size());

        return new PageImpl(restaurantImgs.subList(start,end), pageable, restaurantImgs.size());
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
