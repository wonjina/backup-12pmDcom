package com.gabia.project.internproject.service.recruitMember;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitMember;
import com.gabia.project.internproject.common.exception.BusinessException;
import com.gabia.project.internproject.common.helper.Notification;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RecruitBoardRepository;
import com.gabia.project.internproject.repository.RecruitMemberRepository;
import com.gabia.project.internproject.service.recruitMember.dto.RecruitMemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitMemberSpecification.equalMember;
import static com.gabia.project.internproject.common.helper.customSpecifications.RecruitMemberSpecification.greaterThanDateTime;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Transactional
@RequiredArgsConstructor
public class RecruitMemberService {

    private final RecruitBoardRepository recruitBoardRepository;
    private final RecruitMemberRepository recruitMemberRepository;
    private final MemberRepository memberRepository;

    // 모집글 참여
    public String joinPost(int boardId, int memberId) throws BusinessException {
        RecruitBoard post = recruitBoardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_BOARD.getMsg()));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_MEMBER.getMsg()));

        recruitMemberRepository.save(RecruitMember.builder()
                .recruitBoard(post)
                .member(member).build());

        return Notification.SUCCESS_JOIN.getMsg();
    }

    // 모집글 참여 취소
    public String leavePost(int boardId, int memberId) throws BusinessException {
        recruitBoardRepository.findById(boardId).orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_BOARD.getMsg()));
        memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_MEMBER.getMsg()));

        RecruitMember recruitMember = recruitMemberRepository.findRecruitMemberByRecruitBoardIdAndMemberId(boardId, memberId);
        int countNumber = recruitMemberRepository.countRecruitMembersByRecruitBoardId(boardId);

        if(recruitMember == null) { // 해당 모집글에 멤버가 존재하지 않을 경우
            throw new BusinessException(Notification.NOT_FOUND_PARTICIPATION.getMsg());
        } else if(countNumber > 1) { // 참여 멤버만 삭제
            recruitMemberRepository.delete(recruitMember);
            return Notification.SUCCESS_CANCEL.getMsg();
        } else { // 마지막 참여자가 모집글을 떠날경우 모집글 삭제
            recruitBoardRepository.deleteById(boardId);
            return Notification.SUCCESS_DELETE.getMsg();
        }
    }

    // 사용자 참여 게시판 조회
    public Page<RecruitMemberDto> getUserRecord(LocalDateTime localDateTime, int memberId, Pageable pageable) throws BusinessException {
        memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_MEMBER.getMsg()));
        List<RecruitMemberDto> user = recruitMemberRepository.findAll(where(greaterThanDateTime(localDateTime))
                .and(equalMember(memberId)))
                .stream()
                .map( r -> new RecruitMemberDto(r))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), user.size());

        return new PageImpl(user.subList(start,end), pageable, user.size());
    }


    // 사용자 참여 게시판 조회
    public Page<RecruitMemberDto> getUserRecord2(LocalDateTime localDateTime, int memberId, Pageable pageable) throws BusinessException {
        memberRepository.findById(memberId).orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_MEMBER.getMsg()));
        List<RecruitMemberDto> user = recruitMemberRepository.findAll(where(greaterThanDateTime(localDateTime))
                .and(equalMember(memberId)))
                .stream()
                .map( r -> new RecruitMemberDto(r))
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), user.size());

        return new PageImpl(user.subList(start,end), pageable, user.size());
    }

}