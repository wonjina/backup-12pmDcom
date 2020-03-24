package com.gabia.project.internproject.controller.restaurant;

import com.gabia.project.internproject.common.exception.BusinessException;
import com.gabia.project.internproject.controller.dto.ResponseDto;
import com.gabia.project.internproject.controller.restaurant.dto.ResFilterDto;
import com.gabia.project.internproject.controller.restaurantImg.dto.ResImgFilterDto;
import com.gabia.project.internproject.service.restaurant.RestaurantService;
import com.gabia.project.internproject.service.restaurant.dto.RestaurantDto;
import com.gabia.project.internproject.service.restaurantImg.RestaurantImgService;
import com.gabia.project.internproject.service.restaurantImg.dto.RestaurantImgDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantImgService restaurantImgService;

    @GetMapping("/api/restaurants/{id}/images")
    public ResponseDto restaurantImgDetail(@PathVariable Integer id,
                                           @PageableDefault(page = 0, size = 5) Pageable pageable,
                                           PagedResourcesAssembler<RestaurantImgDto> assembler) throws BusinessException {
        return new ResponseDto(assembler.toModel(restaurantImgService.getRestaurantImgDetail(id, pageable)));
    }

    @GetMapping(value = "/api/restaurants", produces = "application/json; charset=utf8")
    public ResponseDto restaurantsInfo(@ModelAttribute ResFilterDto resInfoReqDto,
                                       Pageable pageable,
                                       PagedResourcesAssembler<RestaurantDto> assembler) {
        return new ResponseDto(assembler.toModel(restaurantService.getRestaurantsList(resInfoReqDto, pageable)));
    }

    @GetMapping(value = "/api/restaurants/{id}", produces = "application/json; charset=utf8")
    public ResponseDto restaurantsDetail(@PathVariable Integer id) {
        return new ResponseDto(restaurantService.getRestaurantDetail(id));
    }


}