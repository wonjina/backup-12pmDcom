package com.gabia.project.internproject.common.helper.customSpecifications;

import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.Restaurant_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class RestaurantSpecifications {

    public static Specification<Restaurant> equalsRestaurantName(String value) {
        if(value == null) {
            return null;
        }
        return (root, query, cb) -> cb.like(root.get(Restaurant_.NAME), "%"+value+"%");
    }

    public static Specification<Restaurant> equalsRestaurantId(Integer value) {
        if(value == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(Restaurant_.id), value);
    }

    public static Specification<Restaurant> fetchReview() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList();
            root.fetch(Restaurant_.REVIEWS, JoinType.LEFT);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
