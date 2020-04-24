package com.gabia.project.internproject.repository.recruitMember;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitMember;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RecruitBoardRepository;
import com.gabia.project.internproject.repository.RecruitMemberRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitMemberSpecification.equalMember;
import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitMemberSpecification.fetchMember;
import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitMemberSpecification.greaterThanDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.jpa.domain.Specification.where;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class RecruitMemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    RecruitMemberRepository recruitMemberRepository;
    
    @Autowired
    RecruitBoardRepository recruitBoardRepository;
    
    @Autowired 
    RestaurantRepository restaurantRepository;
    
    @Autowired 
    MemberRepository memberRepository;

    private Restaurant restaurant;
    private RecruitBoard recruitBoard1, recruitBoard2;
    private Member member1, member2;
    private RecruitMember recruitMember1, recruitMember2;
    private long beforeCount;

    @BeforeEach
    public void setUp() {
        // 음식점
        restaurant = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
                .reviewAmount(1L)
                .rating(1)
                .zipCode("12345")
                .build();

        // 게시글
        recruitBoard1 = RecruitBoard.builder()
                .maxNumber(5)
                .subject("자장면먹을사람")
                .restaurant(restaurant)
                .build();
        recruitBoard2 = RecruitBoard.builder()
                .maxNumber(4)
                .subject("같이먹어요")
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

        // 참여 사원
        recruitMember1 = RecruitMember.builder()
                .member(member1)
                .recruitBoard(recruitBoard1)
                .build();
        recruitMember2 = RecruitMember.builder()
                .member(member2)
                .recruitBoard(recruitBoard1)
                .build();
    }

    @Test
    public void unitTest_GetUserRecord() {
        // save
        restaurantRepository.save(restaurant);
        recruitBoardRepository.save(recruitBoard1);
        memberRepository.save(member1);

        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,01)); // 오늘 00:00:01

        List<RecruitMember> userRecord = recruitMemberRepository.findAll(where(fetchMember()
                .and(greaterThanDateTime(today)))
                .and(equalMember(member1.getId())));
        assertThat(userRecord.size()).isEqualTo(0);

        // recruitBoard1 모집글 member1 참여
        recruitMemberRepository.save(recruitMember1);

        em.flush();
        em.clear();

        List<RecruitMember> userRecord2 = recruitMemberRepository.findAll(where(fetchMember()
                .and(greaterThanDateTime(today)))
                .and(equalMember(member1.getId())));
        assertThat(userRecord2.size()).isEqualTo(1);

        List<RecruitMember> userRecord3 = recruitMemberRepository.findAll(where(fetchMember()
                .and(greaterThanDateTime(null)))
                .and(equalMember(member1.getId())));
        assertThat(userRecord3.size()).isEqualTo(1);
    }

}