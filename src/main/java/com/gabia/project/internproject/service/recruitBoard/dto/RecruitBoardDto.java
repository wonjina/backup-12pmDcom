package com.gabia.project.internproject.service.recruitBoard.dto;

import com.gabia.project.internproject.common.domain.RecruitBoard;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RecruitBoardDto {

    private int boardId;
    private LocalDateTime boardDate;
    private String boardSubject;
    private int maxNumber;
    private long countMember;
    private String restaurantName;
    private String loadAddress;
    private double locationX;
    private double locationY;
    private List<JoinMemberDto> joinMembers;

    public RecruitBoardDto(RecruitBoard recruitBoard){
        this.boardId = recruitBoard.getId();
        this.boardDate = recruitBoard.getDateTime();
        this.boardSubject = recruitBoard.getSubject();
        this.restaurantName = recruitBoard.getRestaurant().getName();
        this.loadAddress = recruitBoard.getRestaurant().getLoadAddress();
        this.locationX = recruitBoard.getRestaurant().getLocationX();
        this.locationY = recruitBoard.getRestaurant().getLocationY();
        this.maxNumber = recruitBoard.getMaxNumber();
        this.countMember = recruitBoard.getAttendMemberCount();
        this.joinMembers = recruitBoard.getRecruitMembers()
                .stream().map(JoinMemberDto::new).collect(Collectors.toList());
    }

}