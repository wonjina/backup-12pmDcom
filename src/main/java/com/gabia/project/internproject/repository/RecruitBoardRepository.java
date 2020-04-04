package com.gabia.project.internproject.repository;

import com.gabia.project.internproject.common.domain.RecruitBoard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RecruitBoardRepository extends JpaRepository<RecruitBoard, Integer>, JpaSpecificationExecutor<RecruitBoard> {

    @EntityGraph(attributePaths = {"restaurant", "recruitMembers", "recruitMembers.member"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<RecruitBoard> findRecruitBoardById(int id);

    @EntityGraph(attributePaths = {"restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<RecruitBoard> findMyBoardsByRecruitMembers(Specification<RecruitBoard> spec, Pageable pageable);

    @EntityGraph(attributePaths = {"recruitMembers", "recruitMembers.member"}, type = EntityGraph.EntityGraphType.LOAD)
    List<RecruitBoard> findAll(Specification<RecruitBoard> spec);

}
