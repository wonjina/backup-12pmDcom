package com.gabia.project.internproject.common.helper.customSpecifications;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.Review;
import com.gabia.project.internproject.common.domain.Review_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewSpecifications {
    public static Specification<Review> gtDateTime(LocalDateTime value) {
        if(value == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(Review_.DATE_TIME), value);
    }

    public static Specification<Review> equalsWriter(Member value) {
        if(value == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(Review_.MEMBER), value);
    }

    public static Specification<Review> equalsRestaurant(Restaurant value) {
        if(value == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(Review_.RESTAURANT), value);
    }

    public static Specification<Review> equalsReviewId(Integer value) {
        if(value == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(Review_.ID), value);
    }

    public static Specification<Review> equalsRating(Integer value) {
        if(value == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(Review_.RATING), value);
    }

    public static Specification<Review> fetchMember() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList();
            root.fetch(Review_.MEMBER);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
