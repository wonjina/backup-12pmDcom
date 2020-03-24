package com.gabia.project.internproject.service.restaurantImg.dto;

import com.gabia.project.internproject.common.domain.RestaurantImg;
import lombok.Getter;

@Getter
public class RestaurantImgDto {

    private int restaurantImgId;
    private int restaurantId;
    private String restaurantImgUrl;

    public RestaurantImgDto(RestaurantImg restaurantImg, String DOMAIN){
        this.restaurantImgId = restaurantImg.getId();
        this.restaurantId = restaurantImg.getRestaurant().getId();
        if(!restaurantImg.getUrl().startsWith("http")) {
            this.restaurantImgUrl = DOMAIN + restaurantImg.getUrl();
        } else {
            this.restaurantImgUrl = restaurantImg.getUrl();
        }
    }
}
