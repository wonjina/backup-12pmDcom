package com.gabia.project.internproject.common.domain;

import com.gabia.project.internproject.common.helper.PatternType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private int id;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    private String department;

    @Column(name = "employee_number")
    private String employeeNumber;

    @Builder
    public Member(String name, String department, String employeeNumber) {
        this.name = name;
        this.department = department;
        this.employeeNumber = employeeNumber;
    }

    private Member(int id) {
        this.id = id;
    }
    private Member(String name) {
        this.name = name;
    }

    public static Member of(String name) {
        if(!StringUtils.hasText(name)) {
            return null;
        } else {
            return new Member(name);
        }
    }

    public static Member of(int id) {
        if(id <= 0) {
            return null;
        }
        return new Member(id);
    }

    public static Member of(Integer id) {
        if(id == null) {
            return null;
        }
        return new Member(id);
    }


    public boolean changeName(String anotherName) {
        if(!PatternType.nullMatch(anotherName)) {
            return false;
        }

        this.name = anotherName;
        return true;
    }

    public boolean changeDepartment(String newDepartment) {
        if(!PatternType.nullMatch(newDepartment)) {
            return false;
        }

        this.department = newDepartment;
        return true;
    }

    public boolean changeEmployeeNumber(String newEmployeeNumber) {
        if(!PatternType.nullMatch(newEmployeeNumber)) {
            return false;
        }

        this.employeeNumber = newEmployeeNumber;
        return true;
    }

}


