package com.gabia.project.internproject.service.ouath.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenVO {
    private String  code;
    private String msg;
    private AccessTokenData data;

}
