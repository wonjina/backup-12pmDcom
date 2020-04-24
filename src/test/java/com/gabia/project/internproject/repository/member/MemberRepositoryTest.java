package com.gabia.project.internproject.repository.member;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    EntityManager em;

    private Long beforeCount;

    @Test
    public void customMethod_Test_findMemberByEmployeeNumber() {
        Member member1 = Member.builder()
                .name("김인턴")
                .department("개발")
                .employeeNumber("GWTEST1")
                .build();
        memberRepository.save(member1);

        Member member2 = Member.builder()
                .name("박인턴")
                .department("기획")
                .employeeNumber("GWTEST2")
                .build();
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findMemberByEmployeeNumber(member1.getEmployeeNumber()).get();
        Member findMember2 = memberRepository.findMemberByEmployeeNumber(member2.getEmployeeNumber()).get();


        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
    }

    @Test
    public void basicCRUD() {
        beforeCount = memberRepository.count();
        Member member1 = Member.builder()
                .name("김인턴")
                .department("개발")
                .employeeNumber("GWTEST1")
                .build();
        memberRepository.save(member1);

        Member member2 = Member.builder()
                .name("박인턴")
                .department("기획")
                .employeeNumber("GWTEST2")
                .build();
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1 = memberRepository.findMemberByEmployeeNumber(member1.getEmployeeNumber()).get();
        findMember2 = memberRepository.findMemberByEmployeeNumber(member2.getEmployeeNumber()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(beforeCount+2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(beforeCount+2);

        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(beforeCount);
    }

}