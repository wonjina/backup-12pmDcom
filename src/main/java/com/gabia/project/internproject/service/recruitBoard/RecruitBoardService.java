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
import com.gabia.project.internproject.service.review.dto.ReviewsDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitBoardSpecification.equalRecruitBoard;
import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitBoardSpecification.greaterThanDateTime;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional(readOnly=true)
@RequiredArgsConstructor
public class RecruitBoardService {

    private final RecruitBoardRepository recruitBoardRepository;
    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final RecruitMemberRepository recruitMemberRepository;

    // 모집글 상세조회 (단건)
    public RecruitBoardDto getRecruitmentDetail(int id) {

        RecruitBoard recruitBoard = recruitBoardRepository.findRecruitBoardById(id);
        RecruitBoardDto recruitBoardDto = new RecruitBoardDto(recruitBoard);

        return recruitBoardDto;
    }

    // 모집글 상세조회 (여러개)
    public RecruitBoardDto getRecruitmentDetailList(List<Integer> ids) {

        List<RecruitBoard> recruitBoard = recruitBoardRepository.findAll(where(equalRecruitBoard(ids)));
        RecruitBoardDto recruitBoardDto = new RecruitBoardDto(recruitBoard.get(0));

        return recruitBoardDto;
    }

    // 모집글 전체조회 (페이징)
    public Page<RecruitBoardListDto> getAllRecruitment(LocalDateTime localDateTime, Pageable pageable) {
        List<RecruitBoardListDto> todayRecruit = recruitBoardRepository.findAll(where(greaterThanDateTime(localDateTime)))
                .stream()
                .map( r -> new RecruitBoardListDto(r))
                .collect(Collectors.toList());

        this.sortByDateTimeList(todayRecruit);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), todayRecruit.size());

        return new PageImpl(todayRecruit.subList(start,end), pageable, todayRecruit.size());
    }

    // 모집글 쓰기
    @Transactional
    public String newPost(PostRequestDto postRequestDto) throws BusinessException {
        Restaurant restaurant = restaurantRepository.findById(postRequestDto.getRestaurantId())
                .orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_RESTAURANT.getMsg()));
        Member member = memberRepository.findById(postRequestDto.getMemberId())
                .orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_MEMBER.getMsg()));

        // 모집글 테이블 insert
        RecruitBoard newRecruitBoard =  recruitBoardRepository.save(RecruitBoard.builder()
                .maxNumber(postRequestDto.getMaxNumber())
                .subject(postRequestDto.getSubject())
                .restaurant(restaurant).build());

        // 모집참여 테이블 insert
        recruitMemberRepository.save(RecruitMember.builder()
                .member(member)
                .recruitBoard(newRecruitBoard).build());

        return Notification.SUCCESS_POST.getMsg();
    }

    private void sortByDateTimeList(List<RecruitBoardListDto> restaurants) {
        Collections.sort(restaurants, new Comparator<RecruitBoardListDto>() {
            @Override
            public int compare(RecruitBoardListDto rec1, RecruitBoardListDto rec2) {
                if(rec1.getBoardDate().isAfter(rec2.getBoardDate())) {
                    return -1;
                } else if(rec1.getBoardDate().isBefore(rec2.getBoardDate())) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
    }

}