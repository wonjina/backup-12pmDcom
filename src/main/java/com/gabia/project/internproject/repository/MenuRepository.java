package com.gabia.project.internproject.repository;

import com.gabia.project.internproject.common.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository <Menu, Integer> {
}
