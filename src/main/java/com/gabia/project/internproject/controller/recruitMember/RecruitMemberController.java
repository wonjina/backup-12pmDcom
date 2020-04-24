package com.gabia.project.internproject.controller.recruitMember;

import com.gabia.project.internproject.common.exception.BusinessException;
import com.gabia.project.internproject.controller.dto.ResponseDto;
import com.gabia.project.internproject.service.recruitBoard.RecruitBoardService;
import com.gabia.project.internproject.service.recruitMember.RecruitMemberService;
import com.gabia.project.internproject.service.recruitMember.dto.RecruitMemberDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class RecruitMemberController {

    private final RecruitMemberService recruitMemberService;
    private final RecruitBoardService recruitBoardService;

    // 모집글 참여
    @PostMapping("/api/boards/recruitment/{boardId}/members/{memberId}")
    public ResponseDto joinRecruitment(@PathVariable int boardId, @PathVariable int memberId) throws BusinessException {
        return new ResponseDto(recruitMemberService.joinPost(boardId, memberId));
    }
    
    // 모집글 참여 취소
    @DeleteMapping("/api/boards/recruitment/{boardId}/members/{memberId}")
    public ResponseDto leaveRecruitment(@PathVariable int boardId, @PathVariable int memberId) throws BusinessException {
        return new ResponseDto(recruitBoardService.leavePost(boardId, memberId));
    }

    // 사용자 참여 정보 조회
    @GetMapping("/api/member/{id}/recruitment")
    public ResponseDto userRecord(@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime localDateTime,
                                  @PathVariable int id, Pageable pageable, PagedResourcesAssembler<RecruitMemberDto> assembler)
            throws BusinessException {
        return new ResponseDto(assembler.toModel(recruitBoardService.getUserRecord(localDateTime, id, pageable)));
    }

}
