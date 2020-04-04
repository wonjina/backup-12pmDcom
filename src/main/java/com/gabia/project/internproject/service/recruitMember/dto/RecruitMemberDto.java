package com.gabia.project.internproject.service.recruitMember.dto;

import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitMember;
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

    public RecruitMemberDto(RecruitMember recruitMember){
        this.boardId = recruitMember.getRecruitBoard().getId();
        this.boardDate = recruitMember.getRecruitBoard().getDateTime();
        this.boardSubject = recruitMember.getRecruitBoard().getSubject();
        this.restaurantName = recruitMember.getRecruitBoard().getRestaurant().getName();

        this.joinMembers = recruitMember.getRecruitBoard().getRecruitMembers()
                                                            .stream()
                                                            .map(JoinMemberDto::new)
                                                            .collect(Collectors.toList());
    }

    public RecruitMemberDto(RecruitBoard recruitBoard, List<RecruitMember> members) {
        this.boardId = recruitBoard.getId();
        this.boardDate = recruitBoard.getDateTime();
        this.boardSubject = recruitBoard.getSubject();
        this.restaurantName = recruitBoard.getRestaurant().getName();
        this.joinMembers = members.stream().map(JoinMemberDto::new).collect(Collectors.toList());;
    }
}