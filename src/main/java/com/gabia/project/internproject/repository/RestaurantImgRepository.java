package com.gabia.project.internproject.repository;

import com.gabia.project.internproject.common.domain.RestaurantImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RestaurantImgRepository extends JpaRepository<RestaurantImg, Integer>, JpaSpecificationExecutor<RestaurantImg> {
}
