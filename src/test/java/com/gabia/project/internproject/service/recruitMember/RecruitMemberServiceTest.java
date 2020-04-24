package com.gabia.project.internproject.service.recruitMember;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitMember;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RecruitBoardRepository;
import com.gabia.project.internproject.repository.RecruitMemberRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.service.recruitBoard.RecruitBoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitMemberSpecification.equalMember;
import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitMemberSpecification.greaterThanDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;

@SpringBootTest
@Transactional
class RecruitMemberServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    RecruitMemberService recruitMemberService;

    @Autowired
    RecruitBoardRepository recruitBoardRepository;

    @Autowired
    RecruitMemberRepository recruitMemberRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RecruitBoardService recruitBoardService;

    private Restaurant restaurant;
    private RecruitBoard recruitBoard, recruitBoard2, recruitBoard3;
    private Member member1, member2, member3, member4;
    private RecruitMember recruitMember1, recruitMember2;
    private long beforeRbCount, beforeRmCount;

    Pageable pageable = PageRequest.of(0, 5);

    @BeforeEach
    public void setUp(){
        // 음식점
        restaurant = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
                .rating(0)
                .reviewAmount(0l)
                .zipCode("12345")
                .build();

        // 게시글
        recruitBoard = RecruitBoard.builder()
                .maxNumber(5)
                .subject("자장면먹을사람")
                .restaurant(restaurant)
                .build();

        recruitBoard2 = RecruitBoard.builder()
                .maxNumber(5)
                .subject("중국집갈사람")
                .restaurant(restaurant)
                .build();


        // 사원
        member1 = Member.builder()
                .name("김인턴")
                .department("개발")
                .employeeNumber("GW12344")
                .build();
        member2 = Member.builder()
                .name("박인턴")
                .department("기획")
                .employeeNumber("GM12345")
                .build();
        member3 = Member.builder()
                .name("김스프링")
                .department("개발")
                .employeeNumber("GW12346")
                .build();
        member4 = Member.builder()
                .name("박자바")
                .department("기획")
                .employeeNumber("GM12347")
                .build();
    }

    @Test
    public void 모집글_참여_Service_테스트() throws Exception {
        beforeRbCount = recruitMemberRepository.count();

        // save
        restaurantRepository.save(restaurant);
        recruitBoardRepository.save(recruitBoard);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // 모집글 참여
        recruitMemberService.joinPost(recruitBoard.getId(),member1.getId());
        recruitMemberService.joinPost(recruitBoard.getId(),member2.getId());

        assertThat(recruitMemberRepository.count()).isEqualTo(beforeRbCount+2);
    }


}