package com.gabia.project.internproject.controller.recruitBoard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitMember;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.paths.Url;
import com.gabia.project.internproject.controller.recruitBoard.requestDto.PostRequestDto;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RecruitBoardRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.repository.RecruitMemberRepository;
import com.gabia.project.internproject.service.recruitBoard.RecruitBoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecruitBoardControllerTest {

    @Autowired
    EntityManager em;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

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

    @Autowired
    RecruitBoardController recruitBoardController;

    private Restaurant restaurant;
    private RecruitBoard recruitBoard1, recruitBoard2;
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
                .rating(1).reviewAmount(1L)
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

        mockMvc = MockMvcBuilders.standaloneSetup(recruitBoardController).build();
    }

    @Test
    public void 모집글_상세조회_컨트롤러() throws Exception  {
        //save
        restaurantRepository.save(restaurant);
        recruitBoardRepository.save(recruitBoard1);
        recruitBoardRepository.save(recruitBoard2);
        memberRepository.save(member1);
        memberRepository.save(member2);
        recruitMemberRepository.save(recruitMember1);
        recruitMemberRepository.save(recruitMember2);

        em.flush();
        em.clear();

        mockMvc.perform(MockMvcRequestBuilders.get(Url.RecruitBoardInfo.getUrl()+"/"+recruitBoard1.getId()))
                .andExpect(status().isOk()) // recruitBoard1 정상 처리 확인
                .andExpect(handler().handlerType(RecruitBoardController.class)) // 담당 컨트롤러 확인
                .andExpect(handler().methodName("detailRecruitment")) // 메소드 이름 확인
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get(Url.RecruitBoardInfo.getUrl()+"/"+recruitBoard2.getId()))
                .andExpect(status().isOk()) // recruitBoard2 정상 처리 확인
                .andExpect(handler().handlerType(RecruitBoardController.class))
                .andExpect(handler().methodName("detailRecruitment"))
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get(Url.RecruitBoardInfo.getUrl()+"abcd"))
                .andExpect(status().isNotFound()) // 404 Not Found
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.get(Url.RecruitBoardInfo.getUrl()+"/abcd"))
                .andExpect(status().isBadRequest()) // 잘못된 파라미터 비정상 처리 확인
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void 모집글_쓰기_컨트롤러() throws Exception {
        // save
        restaurantRepository.save(restaurant);
        memberRepository.save(member1);

        // 새 모집글
        PostRequestDto newPost = new PostRequestDto(member1.getId(), restaurant.getId(), "자장면드실분", 5);

        mockMvc.perform(MockMvcRequestBuilders.post(Url.RecruitBoards.getUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newPost)))
                .andExpect(status().isOk()) // 정상 처리 확인
                .andExpect(handler().handlerType(RecruitBoardController.class)) // 담당 컨트롤러 확인
                .andExpect(handler().methodName("newRecruitment")) // 메소드 이름 확인
                .andDo(MockMvcResultHandlers.print());
    }

}