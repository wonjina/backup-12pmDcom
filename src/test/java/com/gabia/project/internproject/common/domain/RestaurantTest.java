package com.gabia.project.internproject.common.domain;

import com.gabia.project.internproject.common.helper.PatternType;
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
class RestaurantTest {

    public void validate(Restaurant in) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Restaurant>> violations = validator.validate(in);
        for (ConstraintViolation<Restaurant> violation : violations) {
            System.out.println("err->: "+violation.getPropertyPath().toString()+" : "+violation.getMessage());
        }
    }

    @Test
    public void 가게_validation_check() {
        /* Create Entity */
        System.out.println("===========================0");
        Restaurant restaurant = Restaurant.builder().build();
        validate(restaurant);

        System.out.println("===========================1");
        Restaurant restaurant2 = Restaurant.builder().name("")
                                    .cellNumber("").loadAddress("").locationX(0)
                                    .locationY(4.3).zipCode("3").build();
        validate(restaurant2);

        System.out.println("===========================2");
        Restaurant restaurant32 = Restaurant.builder().name("adsf")
                .cellNumber("22").loadAddress("@$@#132").locationX(0)
                .locationY(4.3).zipCode("3").build();
        validate(restaurant32);

        System.out.println("===========================3");
        Restaurant restaurant42 = Restaurant.builder().name("")
                .cellNumber("222222222222222222222").loadAddress("").build();
        validate(restaurant42);

        System.out.println("===========================4");
        Restaurant restaurant22 = Restaurant.builder().name("adsf")
                .cellNumber("1").loadAddress("@$@#132").locationX(0)
                .locationY(4.3).zipCode("3").build();
        validate(restaurant22);

        /* Change Entity */
        assertThat(restaurant2.changeAddress("new 22",1,0)).isTrue();
        assertThat(restaurant2.changeAddress("",0,-2)).isFalse();
        assertThat(restaurant2.changeCellNumber("new 22")).isFalse();
        assertThat(restaurant2.changeCellNumber("1")).isTrue();
        assertThat(restaurant2.changeCellNumber("")).isFalse();
        assertThat(restaurant2.changeCellNumber("aasdwqf")).isFalse();
        assertThat(restaurant2.changeCellNumber("888888888")).isTrue();
        assertThat(restaurant2.changeLoadAddress("new 45tuyuitu8ytf22")).isTrue();
        assertThat(restaurant2.changeLoadAddress("")).isFalse();
        assertThat(restaurant2.changeLocation(1,0)).isTrue();
        assertThat(restaurant2.changeLocation(0,-2)).isTrue();
    }

    @Test
    public void enum클래스테스트(){
        Restaurant restaurant2 = Restaurant.builder().name("ㅁㄴㅇㄹ")
                .cellNumber("333").loadAddress("").locationX(0)
                .locationY(4.3).zipCode("3").build();

        assertThat(restaurant2.changeCellNumber("dfaef")).isFalse();
        assertThat(restaurant2.changeCellNumber("1")).isTrue();
        assertThat(restaurant2.changeCellNumber("new")).isFalse();
        assertThat(restaurant2.changeCellNumber("")).isFalse();
        assertThat(restaurant2.changeCellNumber("888888888")).isTrue();

        assertThat(PatternType.isMatchOne("ㄴㅁㅇㄹ", "KO")).isTrue();
        assertThat(PatternType.isMatchOne("123", "KO")).isFalse();
        assertThat(PatternType.isMatchOne("asdf", "KO")).isFalse();
        assertThat(PatternType.isMatchOne("", "KO")).isFalse();
        assertThat(PatternType.isMatchOne( "", "KO")).isFalse();

        assertThat(PatternType.isMatchOne("asdfASD", "EN")).isTrue();
        assertThat(PatternType.isMatchOne("asdf", "EN")).isTrue();
        assertThat(PatternType.isMatchOne("", "EN")).isFalse();
        assertThat(PatternType.isMatchOne(" ", "EN")).isFalse();
        assertThat(PatternType.isMatchOne("asd123", "EN")).isFalse();
        assertThat(PatternType.isMatchOne("SAD213", "EN")).isFalse();
        assertThat(PatternType.anyMatch("123")).isTrue();
        assertThat(PatternType.anyMatch("SAD213")).isFalse();
        assertThat(PatternType.anyMatch("@#$")).isFalse();
        assertThat(PatternType.anyMatch("한글")).isTrue();

        assertThat(PatternType.isMatchOne("213", "NUM")).isTrue();
        assertThat(PatternType.isMatchOne("@#$", "EN")).isFalse();
        assertThat(PatternType.isMatchOne("SAD213", "NUM")).isFalse();
        assertThat(PatternType.isMatchOne("aedED", "EN")).isTrue();
    }

}