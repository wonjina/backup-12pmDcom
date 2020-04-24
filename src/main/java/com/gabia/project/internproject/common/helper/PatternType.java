package com.gabia.project.internproject.common.helper;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;


/**
 *  입력값 정규식 필터링
 */
@Getter
public enum PatternType {
    KO("^[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]+$"),
    EN("^[a-zA-Z]+$"),
    EN_LOWER("^[a-z]+$"),
    EN_UPPER("^[A-Z]+$"),
    NUM("^[0-9]+$"),
    EMAIL("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]@2,6}$"),
    ONLY_NUMBER("[^0-9]"),
    ;

    private String rex;
    PatternType(String rex) {
        this.rex = rex;
    }

    public static boolean isMatchOne(String targetStr, String type) {
        return Optional.ofNullable(targetStr)
                        .filter(t -> t.length()>0)
                        .map(str -> {
                            return Pattern.compile(PatternType.valueOf(type).getRex())
                                            .matcher(str)
                                            .find();
                        })
                        .orElse(false);
    }

    public static boolean anyMatch(String targetStr) {
        return Optional.ofNullable(targetStr)
                        .filter(t -> t.length()>0)
                        .map(str -> {
                            return Arrays.stream(PatternType.values())
                                    .anyMatch(type -> {
                                        return Pattern.compile(type.getRex())
                                                        .matcher(str)
                                                        .find();
                                    });
                        })
                        .orElse(false);
    }

    public static boolean nullMatch(String targetStr){
        return Optional.ofNullable(targetStr).filter(t -> t.length()>0).isPresent();
    }
}

