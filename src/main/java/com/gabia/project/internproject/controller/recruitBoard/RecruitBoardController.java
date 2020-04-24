package com.gabia.project.internproject.controller.recruitBoard;

import com.gabia.project.internproject.common.domain.RecruitBoard_;
import com.gabia.project.internproject.common.exception.BusinessException;
import com.gabia.project.internproject.controller.dto.ResponseDto;
import com.gabia.project.internproject.controller.recruitBoard.requestDto.PostRequestDto;
import com.gabia.project.internproject.service.recruitBoard.RecruitBoardService;
import com.gabia.project.internproject.service.recruitBoard.dto.RecruitBoardListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class RecruitBoardController {

    private final RecruitBoardService recruitBoardService;

    // 모집글 상세조회
    @GetMapping("/api/boards/recruitment/{id}")
    public ResponseDto detailRecruitment(@PathVariable int id) {
        return new ResponseDto(recruitBoardService.getRecruitmentDetail(id));
    }

    // 모집글 전체보기 (페이징)
    @GetMapping("/api/boards/recruitment")
    public ResponseDto allRecruitment(@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime localDateTime,
                                      @PageableDefault(page = 10, size = 5, sort = RecruitBoard_.DATE_TIME, direction = Sort.Direction.DESC) Pageable pageable,
                                      PagedResourcesAssembler<RecruitBoardListDto> assembler){
        return new ResponseDto(assembler.toModel(recruitBoardService.getAllRecruitment(localDateTime, pageable)));
    }

    // 모집글 쓰기
    @PostMapping("/api/boards/recruitment")
    public ResponseDto newRecruitment(@RequestBody PostRequestDto postRequestDto) throws BusinessException {
        return new ResponseDto(recruitBoardService.newPost(postRequestDto));
    }

}