package com.gabia.project.internproject.service.ouath.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HiworksUserVO {
    @JsonProperty(value = "no")
    private String no;

    @JsonProperty(value = "user_id")
    private String userId;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "member_id")
    private Integer memberId;

    @JsonProperty(value = "office_number")
    private String officeNumber;

    public void initMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void initOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }
}
