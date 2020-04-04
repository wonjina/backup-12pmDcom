package com.gabia.project.internproject.service.recruitBoard;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitMember;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.exception.BusinessException;
import com.gabia.project.internproject.common.helper.Notification;
import com.gabia.project.internproject.controller.recruitBoard.requestDto.PostRequestDto;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RecruitBoardRepository;
import com.gabia.project.internproject.repository.RecruitMemberRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import com.gabia.project.internproject.service.recruitBoard.dto.RecruitBoardDto;
import com.gabia.project.internproject.service.recruitBoard.dto.RecruitBoardListDto;
import com.gabia.project.internproject.service.recruitMember.dto.RecruitMemberDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitBoardSpecification.equalRecMember;
import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitBoardSpecification.equalRecruitBoard;
import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitBoardSpecification.greaterThanDateTime;
import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitMemberSpecification.equalMember;
import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitMemberSpecification.equalRecruitBoardNMember;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class RecruitBoardService {

    private final RecruitBoardRepository recruitBoardRepository;
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final RecruitMemberRepository recruitMemberRepository;

    /**
     * 특정 게시글 상세 정보
     * @param id
     * @return
     */
    public RecruitBoardDto getRecruitmentDetail(int id) {
        RecruitBoard recruitBoard = recruitBoardRepository.findRecruitBoardById(id)
                                        .orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_BOARD.getMsg()));
        return new RecruitBoardDto(recruitBoard);
    }

    /**
     * 게시글 조회
     * @param localDateTime : 날짜 필터
     * @param pageable : 정렬 및 페이징
     * @return
     */
    public Page<RecruitBoardListDto> getAllRecruitment(LocalDateTime localDateTime, Pageable pageable) {
        Page<RecruitBoard> todayRecruit = recruitBoardRepository.findAll(where(greaterThanDateTime(localDateTime)), pageable);

        return new PageImpl(todayRecruit.stream().map(RecruitBoardListDto::new).collect(Collectors.toList())
                            , pageable
                            , todayRecruit.getTotalPages()*todayRecruit.getSize());
    }

    /**
     * 모집글 작성하기
     * @param postRequestDto
     * @return
     * @throws BusinessException
     */
    @Transactional
    public String newPost(PostRequestDto postRequestDto) throws BusinessException {
        //요청 데이터 유효성 검사
        Restaurant restaurant = restaurantRepository.findById(postRequestDto.getRestaurantId())
                                    .orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_RESTAURANT.getMsg()));
        Member member = memberRepository.findById(postRequestDto.getMemberId())
                            .orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_MEMBER.getMsg()));

        //recruitboard 생성
        RecruitBoard newRecruitBoard = RecruitBoard.builder()
                                                .maxNumber(postRequestDto.getMaxNumber())
                                                .subject(postRequestDto.getSubject())
                                                .restaurant(restaurant).build();
        // add recruitemember (casecade 설정 활용하기 위해)
        newRecruitBoard.addRecruitMember(RecruitMember.builder()
                                                        .member(member)
                                                        .recruitBoard(newRecruitBoard).build());
        // 모집글 테이블 insert ( recruitMember도 같이 저장됨. casecade 설정때문에)
        recruitBoardRepository.save(newRecruitBoard);

        return Notification.SUCCESS_POST.getMsg();
    }

    /**
     * 사용자가 참여해온 게시글 리스트
     * @param memberId
     * @param pageable
     * @return
     * @throws BusinessException
     */
    public Page<RecruitMemberDto> getUserRecord(LocalDateTime localDateTime, int memberId, Pageable pageable) throws BusinessException {
        //존재하는 사용자인지 검증
        Member user = memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_MEMBER.getMsg()));

        // 사용자가 참여한 게시글 리스트
        Page<RecruitBoard> boards = recruitBoardRepository.findAll(where(equalRecMember(memberId))
                                                                            .and(greaterThanDateTime(localDateTime))
                                                                    , pageable);

        List<RecruitMemberDto> recruitMemberDtos = boards.stream().map(RecruitMemberDto::new).collect(Collectors.toList());
        return new PageImpl(recruitMemberDtos, pageable, boards.getTotalPages()*boards.getSize());
    }


    /**
     * 모집글 참여 취소
     * @param boardId
     * @param memberId
     * @return
     * @throws BusinessException
     */
    public String leavePost(int boardId, int memberId) throws BusinessException {
        //요청 데이터 검증
        RecruitBoard recruitBoard = recruitBoardRepository.findRecruitBoardById(boardId)
                .orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_BOARD.getMsg()));
        RecruitMember recruitMember = recruitMemberRepository.findRecruitMemberByRecruitBoardIdAndMemberId(boardId, memberId)
                .orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_MEMBER.getMsg()));

        if(recruitBoard.getAttendMemberCount() > 1) { // 참여 멤버만 삭제
            recruitBoard.getRecruitMembers().remove(recruitMember);     //casecade remove
            return Notification.SUCCESS_CANCEL.getMsg();
        } else { // 마지막 참여자가 모집글을 떠날경우 모집글 삭제
            recruitBoardRepository.delete(recruitBoard);
            return Notification.SUCCESS_DELETE.getMsg();
        }
    }

}