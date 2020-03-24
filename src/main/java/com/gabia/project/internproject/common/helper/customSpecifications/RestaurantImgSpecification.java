package com.gabia.project.internproject.common.helper.customSpecifications;

import com.gabia.project.internproject.common.domain.RestaurantImg;
import com.gabia.project.internproject.common.domain.RestaurantImg_;
import com.gabia.project.internproject.common.domain.Restaurant_;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class RestaurantImgSpecification {

    public static Specification<RestaurantImg> equalRestaurant(Integer value) {
        if(value == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.join(RestaurantImg_.RESTAURANT).get(Restaurant_.ID), value);
    }

    public static Specification<RestaurantImg> equalRestaurant(List<Integer> value) {
        if(value == null || value.size() == 0) {
            return null;
        }
        return (root, query, cb) -> root.get(RestaurantImg_.RESTAURANT).in(value);
    }
}