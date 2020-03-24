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
class ReviewImgTest {

    public void validate(ReviewImg in) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<ReviewImg>> violations = validator.validate(in);
        for (ConstraintViolation<ReviewImg> violation : violations) {
            System.out.println("err->: "+violation.getPropertyPath().toString()+" : "+violation.getMessage());
        }
    }
    @Test
    public void 리뷰이미지_validation_check(){

        /* Create Entity */
        System.out.println("===========================0");
        ReviewImg reviewImg = ReviewImg.builder().url("").build();
        validate(reviewImg);

        System.out.println("===========================3");
        ReviewImg reviewImg4 = ReviewImg.builder().url("/").build();
        validate(reviewImg4);

    }

}