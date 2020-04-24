package com.gabia.project.internproject.service.restaurant.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabia.project.internproject.common.domain.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

@NoArgsConstructor
@Relation(collectionRelation = "response", itemRelation = "response")
@Getter
public class RestaurantDto {

    @JsonProperty(value = "rating")
    private double rating;

    @JsonProperty(value = "review_amount")
    private Long reviewsAmount;

    @JsonProperty(value = "restaurant_id")
    private int restaurantId;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "category")
    private String category;

    @JsonProperty(value = "cell_number")
    private String cellNumber;

    @JsonProperty(value = "load_address")
    private String loadAddress;

    @JsonProperty(value = "location_x")
    private double locationX;

    @JsonProperty(value = "location_y")
    private double locationY;

    @JsonProperty(value = "zip_code")
    private String zipCode;

    public RestaurantDto(Restaurant restaurant) {
        this.rating = restaurant.getRating();
        this.reviewsAmount = restaurant.getReviewAmount();
        this.restaurantId =restaurant.getId();
        this.name = restaurant.getName();
        this.category = restaurant.getCategory();
        this.cellNumber = restaurant.getCellNumber();
        this.loadAddress = restaurant.getLoadAddress();
        this.locationX = restaurant.getLocationX();
        this.locationY = restaurant.getLocationY();
        this.zipCode = restaurant.getZipCode();
    }

}
