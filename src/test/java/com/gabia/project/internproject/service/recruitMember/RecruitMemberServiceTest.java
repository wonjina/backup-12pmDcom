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
import com.gabia.project.internproject.service.recruitMember.dto.RecruitMemberDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitBoardSpecification.equalRecruitBoard;
import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitMemberSpecification.equalMember;
import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitMemberSpecification.greaterThanDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.iterable;
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

        recruitBoard3 = RecruitBoard.builder()
                .maxNumber(5)
                .subject("중국집갈사람!!")
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
    public void 모집글조회Test() {

        //게시글 작성될 음식점
        restaurant = Restaurant.builder()
                .name("중국집")
                .cellNumber("029701234")
                .loadAddress("12345")
                .locationX(123)
                .locationY(456)
                .zipCode("12345")
                .build();

        // 게시글 3개
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

        recruitBoard3 = RecruitBoard.builder()
                .maxNumber(5)
                .subject("중국집갈사람!!")
                .restaurant(restaurant)
                .build();

        List<RecruitBoard> recruitBoards = new ArrayList<>();
        recruitBoards.add(recruitBoard);
        recruitBoards.add(recruitBoard2);
        recruitBoards.add(recruitBoard3);

        //게시글 작성자
        member1 = Member.builder()
                .name("김인턴")
                .build();

        //가게 게시글 작성자 저장
        Restaurant createdRes = restaurantRepository.save(restaurant);
        List<RecruitBoard> createdRecruitBoards = recruitBoardRepository.saveAll(recruitBoards);
        Member createdMember = memberRepository.save(member1);

        //게시글 * <- 1 recruitMember 1 -> *멤버 중간 관계 테이블 저장
        List<RecruitMember> recruitMembers = new ArrayList<>();
        for (RecruitBoard createdRecruitBoard : createdRecruitBoards) {
            recruitMembers.add(RecruitMember.builder()
                    .member(createdMember)
                    .recruitBoard(createdRecruitBoard)
                    .build());
        }

        List<RecruitMember> createdRecruitMeber = recruitMemberRepository.saveAll(recruitMembers);

        em.flush();
        em.clear();

        //내가 참여한 게시글 조회
        List<RecruitMember> searchRecruitMembers =
                recruitMemberRepository.findAll(where(greaterThanDateTime(LocalDateTime.of(2020,02,20,00,00,01)))
                                                .and(equalMember(createdMember.getId())));
        assertThat(searchRecruitMembers.size()).isEqualTo(createdRecruitBoards.size());
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

        // 리스트 조회 검증
        List<RecruitMember> all = recruitMemberRepository.findAll();
        assertThat(all.size()).isEqualTo(beforeRbCount+2);
    }

    @Test
    public void 모집글_참여_취소_Service_테스트() throws Exception {
        beforeRmCount = recruitMemberRepository.count();
        beforeRbCount = recruitBoardRepository.count();

        //save
        restaurantRepository.save(restaurant);
        recruitBoardRepository.save(recruitBoard);
        memberRepository.save(member1);
        memberRepository.save(member2);

        //모집글 참여
        RecruitMember recruitMember1 = RecruitMember.builder()
                .recruitBoard(recruitBoard)
                .member(member1)
                .build();
        RecruitMember recruitMember2 = RecruitMember.builder()
                .recruitBoard(recruitBoard)
                .member(member2)
                .build();
        recruitMemberRepository.save(recruitMember1);
        recruitMemberRepository.save(recruitMember2);

        em.flush();
        em.clear();

        //리스트 조회 검증
        List<RecruitBoard> board = recruitBoardRepository.findAll();
        assertThat(board.size()).isEqualTo(beforeRbCount+1); // 모집글 1개
        List<RecruitMember> RecruitMember = recruitMemberRepository.findAll();
        assertThat(RecruitMember.size()).isEqualTo(beforeRmCount+2); // 참여인원 2명

        RecruitBoard recruitBoardTest = recruitBoardRepository.findById(recruitBoard.getId()).get();
        System.out.println(recruitBoardTest.getRecruitMembers().size());

        //member1 참여 취소
        recruitBoardService.leavePost(recruitBoard.getId(), member1.getId());

        em.flush();
        em.clear();

        List<RecruitBoard> board1 = recruitBoardRepository.findAll();
        assertThat(board1.size()).isEqualTo(beforeRbCount+1); // 모집글 1개
        List<RecruitMember> RecruitMember1 = recruitMemberRepository.findAll();
        assertThat(RecruitMember1.size()).isEqualTo(beforeRmCount+1); // 남은 인원 1명

        //member2 참여 취소
        recruitBoardService.leavePost(recruitBoard.getId(), member2.getId());

        List<RecruitBoard> board2 = recruitBoardRepository.findAll();
        assertThat(board2.size()).isEqualTo(beforeRbCount); // 모집글 0개
        List<RecruitMember> RecruitMember2 = recruitMemberRepository.findAll();
        assertThat(RecruitMember2.size()).isEqualTo(beforeRmCount); // 남은 인원 0명
    }

    @Test
    public void 모집글_참여_취소_Service_테스트2() throws Exception {
        beforeRmCount = recruitMemberRepository.count();
        beforeRbCount = recruitBoardRepository.count();

        // save
        restaurantRepository.save(restaurant);
        recruitBoardRepository.save(recruitBoard);
        recruitBoardRepository.save(recruitBoard2);
        recruitBoardRepository.save(recruitBoard3);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        // 모집글 참여
        RecruitMember recruitMember1 = RecruitMember.builder()
                .recruitBoard(recruitBoard)
                .member(member1)
                .build();
        RecruitMember recruitMember2 = RecruitMember.builder()
                .recruitBoard(recruitBoard)
                .member(member2)
                .build();
        RecruitMember recruitMember3 = RecruitMember.builder()
                .recruitBoard(recruitBoard2)
                .member(member3)
                .build();
        RecruitMember recruitMember4 = RecruitMember.builder()
                .recruitBoard(recruitBoard3)
                .member(member4)
                .build();
        recruitMemberRepository.save(recruitMember1);
        recruitMemberRepository.save(recruitMember2);
        recruitMemberRepository.save(recruitMember3);
        recruitMemberRepository.save(recruitMember4);

        em.flush();
        em.clear();

        // 리스트 조회 검증
        List<RecruitBoard> board = recruitBoardRepository.findAll();
        assertThat(board.size()).isEqualTo(beforeRbCount+3);
        List<RecruitMember> RecruitMember = recruitMemberRepository.findAll();
        assertThat(RecruitMember.size()).isEqualTo(beforeRmCount+4);

        // 정상 참여 취소
        recruitBoardService.leavePost(recruitBoard.getId(), member1.getId());

    }

    @Test
    void 사용자_참여_조회_Service_테스트(){
        //save
        restaurantRepository.save(restaurant);
        recruitBoardRepository.save(recruitBoard);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 참여 사원
        recruitMember1 = RecruitMember.builder()
                .member(member1)
                .recruitBoard(recruitBoard)
                .build();
        recruitMember2 = RecruitMember.builder()
                .member(member2)
                .recruitBoard(recruitBoard)
                .build();
        recruitMemberRepository.save(recruitMember1);
        recruitMemberRepository.save(recruitMember2);

        em.flush();
        em.clear();

        LocalDateTime today = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,01)); // 오늘 00:00:01
        LocalDateTime tomorrow = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0,0,01)); // 내일 00:00:01

        // Date 파라미터 값이 있을 경우
        Page<RecruitMemberDto> todayBoards = recruitBoardService.getUserRecord(today, member1.getId(), pageable);
        assertThat(todayBoards.getContent().size()).isEqualTo(1);

        Page<RecruitMemberDto> tomorrowBoards = recruitBoardService.getUserRecord(tomorrow, member1.getId(), pageable);
        assertThat(tomorrowBoards.getContent().size()).isEqualTo(0);

        // Date 파라미터 값이 없을 경우
        Page<RecruitMemberDto> AllBoards = recruitBoardService.getUserRecord(null, member1.getId(), pageable);
        assertThat(AllBoards.getContent().size()).isEqualTo(1);
    }

}