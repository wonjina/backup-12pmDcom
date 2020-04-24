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
class ReviewTest {

    public void validate(Review in) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Review>> violations = validator.validate(in);
        for (ConstraintViolation<Review> violation : violations) {
            System.out.println("err->: "+violation.getPropertyPath().toString()+" : "+violation.getMessage());
        }
    }

    @Test//(expected=EmptyResultDataAccessException.class)
    public void 리뷰_validation_check() {
        /* Create Entity */
        System.out.println("===========================0");
        Review review = Review.builder().comment("").member(null).rating(0).restaurant(null).build();

        validate(review);
        System.out.println(review.getDateTime());
        System.out.println("===========================1");
        Review review2 = Review.builder().comment("wefsdf").rating(1).build();
        validate(review2);

        System.out.println("===========================2");
        Review review3 = Review.builder().comment(null).member(new Member()).rating(2).restaurant(null).build();
        validate(review3);

        System.out.println("===========================3");
        Review review4 = Review.builder().comment("dddddddddddddddddddd")
                                         .member(null).rating(6).restaurant(new Restaurant()).build();
        validate(review4);

        System.out.println("===========================4");
        Review review5 = Review.builder().build();
        validate(review5);
    }
}