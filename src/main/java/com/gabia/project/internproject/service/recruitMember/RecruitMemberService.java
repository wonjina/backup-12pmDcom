package com.gabia.project.internproject.service.recruitMember;

import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitMember;
import com.gabia.project.internproject.common.exception.BusinessException;
import com.gabia.project.internproject.common.helper.Notification;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RecruitBoardRepository;
import com.gabia.project.internproject.repository.RecruitMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
@RequiredArgsConstructor
public class RecruitMemberService {

    private final RecruitBoardRepository recruitBoardRepository;
    private final RecruitMemberRepository recruitMemberRepository;
    private final MemberRepository memberRepository;

    /**
     * 모집글 참여
     * @param boardId
     * @param memberId
     * @return
     * @throws BusinessException
     */
    public String joinPost(int boardId, int memberId) throws BusinessException {
        //요청 데이터 검증
        RecruitBoard post = recruitBoardRepository.findById(boardId)
                .orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_BOARD.getMsg()));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(Notification.NOT_FOUND_MEMBER.getMsg()));
        //멤버 등록
        recruitMemberRepository.save(RecruitMember.builder()
                                                .recruitBoard(post)
                                                .member(member).build());

        return Notification.SUCCESS_JOIN.getMsg();
    }

}