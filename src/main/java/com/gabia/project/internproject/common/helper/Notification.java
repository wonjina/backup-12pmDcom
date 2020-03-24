package com.gabia.project.internproject.common.helper;

import lombok.Getter;

@Getter
public enum Notification {

    SUCCESS_POST("등록 완료되었습니다."),
    SUCCESS_JOIN("참여되었습니다."),
    SUCCESS_CANCEL("취소되었습니다."),
    SUCCESS_DELETE("삭제되었습니다."),

    UNKNOWN_TOKEN("토큰정보가 잘못되었습니다."),
    EXPIRE_TOKEN("토큰이 만료되었습니다."),
    UNAUTHORIZED("로그인이 필요합니다."),

    NOT_FOUND("잘못된 접근입니다."),
    NOT_FOUND_BOARD("모집글이 존재하지 않습니다."),
    NOT_FOUND_MEMBER("멤버 정보가 존재하지 않습니다."),
    NOT_FOUND_PARTICIPATION("존재하지 않는 참여 정보입니다."),
    NOT_FOUND_RESTAURANT("음식점이 존재하지 않습니다.");

    String msg;
    Notification(String msg) {
        this.msg = msg;
    }

}
