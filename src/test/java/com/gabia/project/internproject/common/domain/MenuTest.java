package com.gabia.project.internproject.common.domain;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MenuTest {

    public void validate(Menu in) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Menu>> violations = validator.validate(in);
        for (ConstraintViolation<Menu> violation : violations) {
            System.out.println("err->: "+violation.getPropertyPath().toString()+" : "+violation.getMessage());
        }
    }
    @Test
    public void 메뉴_validation_check(){
        /* Create Entity */
        System.out.println("===========================0");
        Menu menu = Menu.builder().name("asdf").price("22").restaurant(new Restaurant()).build();
        validate(menu);

        System.out.println("===========================1");
        Menu menu2 = Menu.builder().name("asdf").restaurant(new Restaurant()).build();
        validate(menu2);

        System.out.println("===========================2");
        Menu menu3 = Menu.builder().name("").restaurant(new Restaurant()).build();
        validate(menu3);

        System.out.println("===========================3");
        Menu menu4 = Menu.builder().name("asdf").price("1").restaurant(new Restaurant()).build();
        validate(menu4);

        System.out.println("===========================4");
        Menu menu5 = Menu.builder().name("asdf").price("1").build();
        validate(menu5);

        /* Change Entity */
        assertThat(menu.changePrice("22")).isTrue();
        assertThat(menu.changePrice("1")).isTrue();
        assertThat(menu.changePrice("0")).isTrue();
        assertThat(menu.changePrice("-2")).isFalse();
    }

}