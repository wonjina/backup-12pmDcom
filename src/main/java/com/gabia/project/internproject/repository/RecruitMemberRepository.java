package com.gabia.project.internproject.repository;

import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface RecruitMemberRepository extends JpaRepository<RecruitMember, Integer>, JpaSpecificationExecutor<RecruitMember> {

    @EntityGraph(attributePaths = {"recruitBoard", "member"}, type = EntityGraph.EntityGraphType.LOAD)
    List<RecruitMember> findAll(Specification<RecruitMember> spec);

    @EntityGraph(attributePaths = {"recruitBoard", "member"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<RecruitMember> findAll(Specification<RecruitMember> spec, Pageable pageable);

    Optional<RecruitMember> findRecruitMemberByRecruitBoardIdAndMemberId(int boardId, int memberId);

}