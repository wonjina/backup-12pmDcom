package com.gabia.project.internproject.controller.recruitBoard.requestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostRequestDto {

    private int memberId;
    private int restaurantId;
    private String subject;
    private int maxNumber;

    @Builder
    public PostRequestDto(int memberId, int restaurantId, String subject, int maxNumber){
        this.memberId = memberId;
        this.restaurantId = restaurantId;
        this.subject = subject;
        this.maxNumber = maxNumber;
    }
}
