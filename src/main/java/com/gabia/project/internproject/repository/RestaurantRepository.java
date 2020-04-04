package com.gabia.project.internproject.repository;

import com.gabia.project.internproject.common.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer>, JpaSpecificationExecutor<Restaurant> {

    @EntityGraph(attributePaths = {"reviews"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Restaurant> findAll(Specification<Restaurant> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"reviews"}, type = EntityGraph.EntityGraphType.LOAD)
    Restaurant findRestaurantWithReviewsById(Integer id);


}
