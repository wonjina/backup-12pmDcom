package com.gabia.project.internproject.controller.recruitMember;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitMember;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.paths.Url;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RecruitBoardRepository;
import com.gabia.project.internproject.repository.RecruitMemberRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecruitMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    RecruitBoardRepository recruitBoardRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RecruitMemberRepository recruitMemberRepository;

    @Autowired
    RecruitMemberController recruitMemberController;

    private Restaurant restaurant;
    private RecruitBoard recruitBoard;
    private Member member1, member2;
    private RecruitMember recruitMember1, recruitMember2;

    @BeforeEach
    public void setUp(){
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
        recruitBoard = RecruitBoard.builder()
                .maxNumber(5)
                .subject("자장면먹을사람")
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

        mockMvc = MockMvcBuilders.standaloneSetup(recruitMemberController).build();
    }

    @Test
    public void 모집글_참여_컨트롤러_테스트() throws Exception {
        //save
        restaurantRepository.save(restaurant);
        recruitBoardRepository.save(recruitBoard);
        memberRepository.save(member1);
        memberRepository.save(member2);

        mockMvc.perform(MockMvcRequestBuilders
                .post(Url.RecruitBoards.getUrl()+"/"+recruitBoard.getId()+Url.RecruitMember.getUrl()+"/"+member1.getId()))
                .andExpect(status().isOk()) //recruitBoard, member1 정상 처리 확인
                .andExpect(handler().handlerType(RecruitMemberController.class)) //담당 컨트롤러 확인
                .andExpect(handler().methodName("joinRecruitment")) //메소드 이름 확인
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders
                .post(Url.RecruitBoards.getUrl()+"/"+recruitBoard.getId()+Url.RecruitMember.getUrl()+"/"+member2.getId()))
                .andExpect(status().isOk()) //recruitBoard, member2 정상 처리 확인
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders
                .post(Url.RecruitBoards.getUrl()+"/abcd"+Url.RecruitMember.getUrl()+"/"+member1.getId()))
                .andExpect(status().isBadRequest()) //잘못된 recruitBoard id 비정상 처리 확인
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders
                .post(Url.RecruitBoards.getUrl()+"/"+recruitBoard.getId()+Url.RecruitMember.getUrl()+"/abcd"))
                .andExpect(status().isBadRequest()) //잘못된 member id 비정상 처리 확인
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders
                .post(Url.RecruitBoards.getUrl()+recruitBoard.getId()+Url.RecruitMember.getUrl()+member1.getId()))
                .andExpect(status().isNotFound()) //잘못된 url 비정상 처리 확인
                .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    public void 모집글_참여_취소_테스트() throws Exception {
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

        mockMvc.perform(MockMvcRequestBuilders
                .delete(Url.RecruitBoards.getUrl()+"/abcd"+Url.RecruitMember.getUrl()+"/"+member1.getId()))
                .andExpect(status().isBadRequest()) //잘못된 recruitBoard id 비정상 처리 확인
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders
                .delete(Url.RecruitBoards.getUrl()+"/"+recruitBoard.getId()+Url.RecruitMember.getUrl()+"/abcd"))
                .andExpect(status().isBadRequest()) //잘못된 member id 비정상 처리 확인
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders
                .delete(Url.RecruitBoards.getUrl()+"/"+recruitBoard.getId()+Url.RecruitMember.getUrl()+"/"+member1.getId()))
                .andExpect(status().isOk()) //recruitBoard1, member1 정상 처리 확인
                .andExpect(handler().handlerType(RecruitMemberController.class)) //담당 컨트롤러 확인
                .andExpect(handler().methodName("leaveRecruitment")) //메소드 이름 확인
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders
                .delete(Url.RecruitBoards.getUrl()+recruitBoard.getId()+Url.RecruitMember.getUrl()+member1.getId()))
                .andExpect(status().isNotFound()) //잘못된 url 비정상 처리 확인
                .andDo(MockMvcResultHandlers.print());
    }

}