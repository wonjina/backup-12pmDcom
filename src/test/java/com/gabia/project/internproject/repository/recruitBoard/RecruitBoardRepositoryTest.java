package com.gabia.project.internproject.repository.recruitBoard;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitMember;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.exception.BusinessException;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RecruitBoardRepository;
import com.gabia.project.internproject.repository.RecruitMemberRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RecruitBoardRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    RecruitBoardRepository recruitBoardRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RecruitMemberRepository recruitMemberRepository;

    private Restaurant restaurant;
    private RecruitBoard recruitBoard1, recruitBoard2;
    private Member member1, member2;
    private RecruitMember recruitMember1, recruitMember2;
    private long beforeRbCount, beforeRmCount;

    @BeforeEach
    public void setUp() {
        // 음식점
        restaurant = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
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
    public void basicCRUD() {
        beforeRbCount = recruitBoardRepository.count();

        // save
        restaurantRepository.save(restaurant);
        recruitBoardRepository.save(recruitBoard1);
        recruitBoardRepository.save(recruitBoard2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        recruitMemberRepository.save(recruitMember1);
        recruitMemberRepository.save(recruitMember2);

        em.flush();

        // 단건 조회 검증
        RecruitBoard findBoard1 = recruitBoardRepository.findById(recruitBoard1.getId()).get();
        RecruitBoard findBoard2 = recruitBoardRepository.findById(recruitBoard2.getId()).get();
        assertThat(findBoard1).isEqualTo(recruitBoard1);
        assertThat(findBoard2).isEqualTo(recruitBoard2);

        // 리스트 조회 검증
        List<RecruitBoard> all = recruitBoardRepository.findAll();
        assertThat(all.size()).isEqualTo(beforeRbCount+2);

        // 카운트 검증
        long count = recruitBoardRepository.count();
        assertThat(count).isEqualTo(beforeRbCount+2);

        // 삭제 검증
        recruitBoardRepository.delete(recruitBoard1);
        recruitBoardRepository.delete(recruitBoard2);
        long deletedCount = recruitBoardRepository.count();
        assertThat(deletedCount).isEqualTo(beforeRbCount);
    }

    @Test
    public void 엔티티_연관관계_메소드_테스트() throws Exception {
        beforeRbCount = recruitBoardRepository.count();
        beforeRmCount = recruitMemberRepository.count();

        // save
        restaurantRepository.save(restaurant);
        memberRepository.save(member1);
        memberRepository.save(member2);

        /**
         * 삽입 Test
         */
        recruitBoard1.addRecruitMember(recruitMember1);
        recruitBoard1.addRecruitMember(recruitMember2);

        recruitBoardRepository.save(recruitBoard1);

        RecruitBoard detailRecruit1 = recruitBoardRepository.findById(recruitBoard1.getId()).get();
        assertThat(detailRecruit1.getAttendMemberCount()).isEqualTo(recruitBoard1.getRecruitMembers().size());

        RecruitBoard recruitBoardTest = recruitBoardRepository.findRecruitBoardById(recruitBoard1.getId()).orElseThrow(Exception::new);

        assertThat(recruitBoardTest.getRecruitMembers().get(0).getId()).isEqualTo(recruitMember1.getId());


        /**
         * 삭제 Test
         */
        assertThat(beforeRbCount).isNotEqualTo(recruitBoardRepository.count());
        assertThat(beforeRmCount).isNotEqualTo(recruitMemberRepository.count());

        em.remove(recruitBoard1);
        em.flush();
        em.clear();

        assertThat(beforeRbCount).isEqualTo(recruitBoardRepository.count());
        assertThat(beforeRmCount).isEqualTo(recruitMemberRepository.count());
    }

    @Test
    public void 모집글_상세조회_테스트() throws Exception {
        // save
        restaurantRepository.save(restaurant);
        recruitBoardRepository.save(recruitBoard1);
        recruitBoardRepository.save(recruitBoard2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        recruitMemberRepository.save(recruitMember1);
        recruitMemberRepository.save(recruitMember2);

        em.flush();
        em.clear();

        RecruitBoard detailRecruit1 = recruitBoardRepository.findRecruitBoardById(recruitBoard1.getId()).orElseThrow(Exception::new);
        assertThat(detailRecruit1.getRestaurant().getName()).isEqualTo(recruitBoard1.getRestaurant().getName()); // recruitBoard1의 음식점 이름
        assertThat(detailRecruit1.getSubject()).isEqualTo(recruitBoard1.getSubject()); // recruitBoard1의 제목
        assertThat(detailRecruit1.getMaxNumber()).isEqualTo(recruitBoard1.getMaxNumber()); // recruitBoard1의 최대인원
        assertThat(detailRecruit1.getAttendMemberCount()).isEqualTo(2); // recruitBoard1의 참여인원

        RecruitBoard detailRecruit2 = recruitBoardRepository.findRecruitBoardById(recruitBoard2.getId()).orElseThrow(Exception::new);
        assertThat(detailRecruit2.getRestaurant().getName()).isEqualTo(recruitBoard2.getRestaurant().getName()); // recruitBoard2의 음식점 이름
        assertThat(detailRecruit2.getSubject()).isEqualTo(recruitBoard2.getSubject()); // recruitBoard2의 제목
        assertThat(detailRecruit2.getMaxNumber()).isEqualTo(recruitBoard2.getMaxNumber()); // recruitBoard2의 최대인원
        assertThat(detailRecruit2.getAttendMemberCount()).isEqualTo(0); // recruitBoard2의 참여인원
    }

}