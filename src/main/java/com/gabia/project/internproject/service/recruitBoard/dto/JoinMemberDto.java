package com.gabia.project.internproject.service.recruitBoard.dto;

import com.gabia.project.internproject.common.domain.RecruitMember;
import lombok.Getter;

@Getter
public class JoinMemberDto {

    private int memberId;
    private String name;
    private String department;

    public JoinMemberDto(RecruitMember joinMember) {
        this.memberId = joinMember.getMember().getId();
        this.name = joinMember.getMember().getName();
        this.department = joinMember.getMember().getDepartment();
    }

}
