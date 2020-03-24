package com.gabia.project.internproject.repository;

import com.gabia.project.internproject.common.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findMemberByEmployeeNumber(String empNum);
}
