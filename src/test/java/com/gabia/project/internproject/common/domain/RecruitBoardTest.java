package com.gabia.project.internproject.common.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


@SpringBootTest
@Transactional
class RecruitBoardTest {

    public void validateRecruitBoard(RecruitBoard in) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<RecruitBoard>> violations = validator.validate(in);
        for (ConstraintViolation<RecruitBoard> violation : violations) {
            System.out.println("err->: "+violation.getPropertyPath().toString()+" : "+violation.getMessage());
        }
    }

    @Test
    public void 게시글_생성_테스트() {

        System.out.println("===========================0");
        RecruitBoard recruitBoard = RecruitBoard.builder().maxNumber(5).subject("자장면먹을사람").restaurant(new Restaurant()).build();
        validateRecruitBoard(recruitBoard); // no error

        System.out.println("===========================1");
        RecruitBoard recruitBoard1 = RecruitBoard.builder().maxNumber(1).subject("자장면먹을사람").restaurant(new Restaurant()).build();
        validateRecruitBoard(recruitBoard1); // max_number error

        System.out.println("===========================2");
        RecruitBoard recruitBoard2 = RecruitBoard.builder().maxNumber(3).subject("").restaurant(new Restaurant()).build();
        validateRecruitBoard(recruitBoard2); // subject error

        System.out.println("===========================3");
        RecruitBoard recruitBoard3 = RecruitBoard.builder().maxNumber(3).subject("자장면").restaurant(new Restaurant()).build();
        validateRecruitBoard(recruitBoard3); // subject error
    }

}