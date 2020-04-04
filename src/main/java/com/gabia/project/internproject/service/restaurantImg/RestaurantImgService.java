package com.gabia.project.internproject.service.restaurantImg;

import com.gabia.project.internproject.common.domain.RestaurantImg;
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

    /**
     * 특정 가게 이미지 상세 조회
     * @param id : 가게 기본키
     * @param pageable : 정렬 및 페이징
     * @return
     */
    public Page<RestaurantImgDto> getRestaurantImgDetail(Integer id, Pageable pageable) {
        Page<RestaurantImg> restaurantImgs = restaurantImgRepository.findAll(where(equalRestaurant(id)), pageable);

        //entity -> dto 변환 및 page list 생성
        return new PageImpl(restaurantImgs.stream().map(r -> new RestaurantImgDto(r, DOMAIN_URL)).collect(Collectors.toList()),
                            pageable,
                            restaurantImgs.getTotalPages() * restaurantImgs.getSize());
    }

    /**
     * 음식점 이미지 리스트 조회
     * @param resImgFilterDto :
     * @param pageable : 정렬 및 페이징
     * @return
     * @throws BusinessException
     */
    public List<RestaurantImgDto> getRestaurantImgList(ResImgFilterDto resImgFilterDto, Pageable pageable) throws BusinessException {
        //get 정렬된 식당 리스트 -> get id list
        List<Integer> resIds = restaurantService.getRestaurantsList(new ResFilterDto(), pageable)
                                                .getContent()
                                                .stream()
                                                .map(RestaurantDto::getRestaurantId)
                                                .collect(Collectors.toList());

        //식당 ids 로 식당 이미지 get
        return restaurantImgRepository.findAll(where(equalRestaurant(resIds)))
                                        .stream()
                                        .filter(distinctByKey(resImg ->resImg.getRestaurant().getId())) //가게별 하나의 이미지만 list에 추가하기 위해
                                        .map(r -> new RestaurantImgDto(r, DOMAIN_URL))
                                        .collect(Collectors.toList());
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
