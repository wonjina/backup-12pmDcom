package com.gabia.project.internproject.service.ouath.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessTokenData {
    private String access_token;
    private String refresh_token;
    private String office_no;
    private String user_no;

}
