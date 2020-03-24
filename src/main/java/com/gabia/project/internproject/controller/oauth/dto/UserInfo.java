package com.gabia.project.internproject.controller.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gabia.project.internproject.service.ouath.vo.HiworksUserVO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInfo {
    HiworksUserVO user;

    @Builder
    public UserInfo(HiworksUserVO user) {
        this.user = user;
    }
}
