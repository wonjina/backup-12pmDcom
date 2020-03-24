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
class MemberTest {

    public void validatePerson(Member in) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<Member>> violations = validator.validate(in);
        for (ConstraintViolation<Member> violation : violations) {
            System.out.println("err->: "+violation.getPropertyPath().toString()+" : "+violation.getMessage());
        }
    }

    @Test
    public void 멤버생성_데이터조회(){

        /* Create Entity */
        System.out.println("===========================0");
        Member member = Member.builder().name("안녕").department("개발").employeeNumber("222342").build();
        validatePerson(member);//no err

        System.out.println("===========================1");
        Member member1 = Member.builder().name("").department("개발").employeeNumber("222342").build();
        validatePerson(member1);

        System.out.println("===========================2");
        Member member2 = Member.builder().name("test1").department("").employeeNumber("222342").build();
        validatePerson(member2);

        System.out.println("===========================3");
        Member member3 = Member.builder().name("test1").department("개발").employeeNumber("").build();
        validatePerson(member3);

        System.out.println("===========================4");
        Member member4 = Member.builder().name(" ").department("개발").employeeNumber("222342").build();
        validatePerson(member4);

        System.out.println("===========================5");
        Member member5 = Member.builder().name("test1").department("@").employeeNumber("222342").build();
        validatePerson(member5);

        System.out.println("===========================6");
        Member member6 = Member.builder().name("test1").department("개발").employeeNumber("$$").build();
        validatePerson(member6);

        System.out.println("===========================7");
        Member member7 = Member.builder().name("4545").department("개발").employeeNumber("222342").build();
        validatePerson(member7);

        System.out.println("===========================8");

        /* Change Entity */
        boolean a = member.changeName("changeName");
        System.out.println(member.getName()+","+a);

        assertThat(member.changeName("변경")).isTrue();
        assertThat(member.changeName("")).isFalse();
        assertThat(member.changeDepartment("변경")).isTrue();
        assertThat(member.changeDepartment("")).isFalse();
        assertThat(member.changeEmployeeNumber("변경")).isTrue();
        assertThat(member.changeEmployeeNumber("")).isFalse();
    }

}