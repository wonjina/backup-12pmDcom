package com.gabia.project.internproject.service.recruitBoard.dto;

import com.gabia.project.internproject.common.domain.RecruitBoard;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecruitBoardListDto {

    private int boardId;
    private LocalDateTime boardDate;
    private String boardSubject;
    private int maxNumber;
    private long countMember;
    private String restaurantName;

    public RecruitBoardListDto(RecruitBoard recruitBoard){
        this.boardId = recruitBoard.getId();
        this.boardDate = recruitBoard.getDateTime();
        this.boardSubject = recruitBoard.getSubject();
        this.restaurantName = recruitBoard.getRestaurant().getName();
        this.maxNumber = recruitBoard.getMaxNumber();
        this.countMember = recruitBoard.getAttendMemberCount();
    }

}
