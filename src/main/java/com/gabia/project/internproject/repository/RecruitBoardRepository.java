package com.gabia.project.internproject.repository;

import com.gabia.project.internproject.common.domain.RecruitBoard;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface RecruitBoardRepository extends JpaRepository<RecruitBoard, Integer>, JpaSpecificationExecutor<RecruitBoard> {

    @EntityGraph(attributePaths = {"restaurant", "recruitMembers.member"}, type = EntityGraph.EntityGraphType.LOAD)
    RecruitBoard findRecruitBoardById(int id);

    @EntityGraph(attributePaths = {"restaurant", "recruitMembers", "recruitMembers.member"}, type = EntityGraph.EntityGraphType.LOAD)
    List<RecruitBoard> findAll(Specification<RecruitBoard> spec);

}
