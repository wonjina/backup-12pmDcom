package com.gabia.project.internproject.service.recruitMember.dto;

import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.service.recruitBoard.dto.JoinMemberDto;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RecruitMemberDto {

    private int boardId;
    private LocalDateTime boardDate;
    private String boardSubject;
    private String restaurantName;
    private List<JoinMemberDto> joinMembers;

    public RecruitMemberDto(RecruitBoard recruitBoard) {
        this.boardId = recruitBoard.getId();
        this.boardDate = recruitBoard.getDateTime();
        this.boardSubject = recruitBoard.getSubject();
        this.restaurantName = recruitBoard.getRestaurant().getName();
        this.joinMembers = recruitBoard.getRecruitMembers().stream().map(JoinMemberDto::new).collect(Collectors.toList());
    }
}