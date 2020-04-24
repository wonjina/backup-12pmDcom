package com.gabia.project.internproject.service.recruitBoard;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitMember;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.controller.recruitBoard.requestDto.PostRequestDto;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RecruitBoardRepository;
import com.gabia.project.internproject.repository.RecruitMemberRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.service.recruitBoard.dto.RecruitBoardDto;
import com.gabia.project.internproject.service.recruitBoard.dto.RecruitBoardListDto;
import com.gabia.project.internproject.service.recruitMember.dto.RecruitMemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RecruitBoardServiceTest {

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

    @Autowired
    RecruitBoardService recruitBoardService;

    private Restaurant restaurant;
    private RecruitBoard recruitBoard1, recruitBoard2;
    private Member member1, member2;
    private RecruitMember recruitMember1, recruitMember2;
    private long beforeRbCount, beforeRmCount;

    Pageable pageable = PageRequest.of(0,5);

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
                .reviewAmount(0l)
                .rating(0)
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
    void  특정_게시글_상세_정보_Service_테스트(){
        //save
        restaurantRepository.save(restaurant);
        recruitBoardRepository.save(recruitBoard1);
        recruitBoardRepository.save(recruitBoard2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        recruitMemberRepository.save(recruitMember1);
        recruitMemberRepository.save(recruitMember2);

        RecruitBoardDto detailRecruit1 = recruitBoardService.getRecruitmentDetail(recruitBoard1.getId());
        assertThat(detailRecruit1.getBoardSubject()).isEqualTo(recruitBoard1.getSubject()); //recruitBoard1의 제목
        assertThat(detailRecruit1.getMaxNumber()).isEqualTo(recruitBoard1.getMaxNumber()); //recruitBoard1의 최대인원
        assertThat(detailRecruit1.getLoadAddress()).isEqualTo(recruitBoard1.getRestaurant().getLoadAddress()); //recruitBoard1의 음식점 주소
        assertThat(detailRecruit1.getRestaurantName()).isEqualTo(recruitBoard1.getRestaurant().getName()); //recruitBoard1의 음식점 이름
        assertThat(detailRecruit1.getCountMember()).isEqualTo(recruitBoard1.getAttendMemberCount()); //recruitBoard1의 참여인원 수
    }

    @Test
    void 모집글_조회_Service_테스트(){
        beforeRbCount = recruitBoardRepository.count();

        //save
        restaurantRepository.save(restaurant);
        recruitBoardRepository.save(recruitBoard1);
        recruitBoardRepository.save(recruitBoard2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        recruitMemberRepository.save(recruitMember1);
        recruitMemberRepository.save(recruitMember2);


        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,01)); // 오늘 00:00:01
        LocalDateTime tomorrow = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0,0,01)); // 내일 00:00:01

        // Date 파라미터 값이 있을 경우
        Page<RecruitBoardListDto> todayRecruit = recruitBoardService.getAllRecruitment(today, pageable);
        assertThat(todayRecruit.getContent().size()).isEqualTo(2);

        Page<RecruitBoardListDto> tomorrowRecruit = recruitBoardService.getAllRecruitment(tomorrow, pageable);
        assertThat(tomorrowRecruit.getContent().size()).isEqualTo(0);
    }

    @Test
    void 모집글_쓰기_Service_테스트() {
        beforeRbCount = recruitBoardRepository.count();
        beforeRmCount = recruitMemberRepository.count();

        restaurantRepository.save(restaurant);
        memberRepository.save(member1);

        List<RecruitBoard> board = recruitBoardRepository.findAll();
        assertThat(board.size()).isEqualTo(beforeRbCount);

        // 새 모집글
        recruitBoardService.newPost(PostRequestDto.builder()
                                                .restaurantId(restaurant.getId())
                                                .memberId(member1.getId())
                                                .subject("오늘은 햄버거")
                                                .maxNumber(5)
                                                .build());

        em.flush();
        em.clear();

        List<RecruitBoard> board2 = recruitBoardRepository.findAll();
        assertThat(board2.size()).isEqualTo(beforeRbCount+1);

        List<RecruitMember> member = recruitMemberRepository.findAll();
        assertThat(member.size()).isEqualTo(beforeRmCount+1);
    }

    @Test
    void 사용자가_참여해온_게시글_리스트() {
        restaurantRepository.save(restaurant);

        Member memTest = memberRepository.save(member1);
        memberRepository.save(member2);

        recruitBoardRepository.save(recruitBoard1);
        recruitBoard1.addRecruitMember(recruitMemberRepository.save(recruitMember1));
        recruitBoard1.addRecruitMember(recruitMemberRepository.save(RecruitMember.builder().member(member2).recruitBoard(recruitBoard1).build()));


        List<RecruitMemberDto> recruitMembers = recruitBoardService.getUserRecord(null, memTest.getId(), pageable).getContent();
        assertThat(recruitMembers.size()).isEqualTo(1);
        assertThat(recruitMembers.get(0).getJoinMembers().size()).isEqualTo(2);
    }


    @Test
    public void 모집글_참여_취소_Service_테스트() throws Exception {
        beforeRbCount = recruitBoardRepository.count();
        beforeRmCount = recruitMemberRepository.count();

        //save
        restaurantRepository.save(restaurant);
        memberRepository.save(member1);
        memberRepository.save(member2);

        recruitBoardRepository.save(recruitBoard1);


        recruitBoard1.addRecruitMember(recruitMemberRepository.save(RecruitMember.builder()
                .recruitBoard(recruitBoard1)
                .member(member1)
                .build()));
        recruitBoard1.addRecruitMember(recruitMemberRepository.save(RecruitMember.builder()
                .recruitBoard(recruitBoard1)
                .member(member2)
                .build()));

        assertThat(recruitMemberRepository.count()).isEqualTo(beforeRmCount+2); // 남은 인원 2명
        //member1 참여 취소
        recruitBoardService.leavePost(recruitBoard1.getId(), member1.getId());

        assertThat(recruitBoardRepository.count()).isEqualTo(beforeRbCount+1); // 모집글 1개
        assertThat(recruitMemberRepository.count()).isEqualTo(beforeRmCount+1); // 남은 인원 1명

        //member2 참여 취소
        recruitBoardService.leavePost(recruitBoard1.getId(), member2.getId());

        assertThat(recruitBoardRepository.count()).isEqualTo(beforeRbCount); // 모집글 0개
        assertThat(recruitMemberRepository.count()).isEqualTo(beforeRmCount); // 남은 인원 0명
    }

}