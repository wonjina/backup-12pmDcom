package com.gabia.project.internproject.controller.restaurantImg;

import com.gabia.project.internproject.common.domain.Restaurant_;
import com.gabia.project.internproject.common.exception.BusinessException;
import com.gabia.project.internproject.controller.dto.ResponseDto;
import com.gabia.project.internproject.controller.restaurantImg.dto.ResImgFilterDto;
import com.gabia.project.internproject.service.restaurantImg.RestaurantImgService;
import com.gabia.project.internproject.service.restaurantImg.dto.RestaurantImgDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestaurantImgController {

    private final RestaurantImgService restaurantImgService;

    // 음식점 이미지 조회
    @GetMapping("/api/images/restaurants")
    public ResponseDto restaurantImgList(@ModelAttribute ResImgFilterDto resImgFilterDto,
                                         @PageableDefault(page = 0, size = 5, sort = Restaurant_.RATING, direction = Sort.Direction.DESC) Pageable pageable)
                                            throws BusinessException {
        return new ResponseDto(restaurantImgService.getRestaurantImgList(resImgFilterDto, pageable));
    }
}
