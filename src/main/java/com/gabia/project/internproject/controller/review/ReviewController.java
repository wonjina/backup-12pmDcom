package com.gabia.project.internproject.controller.review;

import com.gabia.project.internproject.controller.dto.ResponseDto;
import com.gabia.project.internproject.controller.review.requestDto.ReviewFilterDto;
import com.gabia.project.internproject.controller.review.requestDto.ReviewPostDto;
import com.gabia.project.internproject.service.review.ReviewService;
import com.gabia.project.internproject.service.review.dto.ReviewsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;


@CrossOrigin
@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * 리뷰 리스트 조회
     * @param reviewFilterDto : 필터 (where  절) 조건 정보를 담고 있는 Dto
     * @return : review 정보 및 작성자 정보
     */
    @GetMapping(value = "/api/reviews/restaurant", produces = "application/json; charset=utf8")
    public ResponseDto reviewsList(@ModelAttribute ReviewFilterDto reviewFilterDto,
                                   @PageableDefault(size = 20, page = 0) Pageable pageable,
                                   PagedResourcesAssembler<ReviewsDto> assembler) {
        return new ResponseDto(assembler.toModel(reviewService.getReviewsList(reviewFilterDto, pageable)));
    }

    /**
     * 리뷰 작성하기
     * @param errors : vaildation error check
     * @return  등록된 review id
     * @throws
     */
    @PostMapping(value = "/api/reviews/restaurant", produces = "application/json; charset=utf8")
    public ResponseDto writeReview(@RequestBody ReviewPostDto reviewPostDto, Errors errors) throws ValidationException {
        if (errors.hasErrors()) {
            throw new ValidationException("필수 값을 입력해주세요.");
        }
        return new ResponseDto(reviewService.writeReview(reviewPostDto));
    }
}
