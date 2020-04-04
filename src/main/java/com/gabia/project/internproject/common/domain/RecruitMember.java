package com.gabia.project.internproject.common.domain;

import io.jsonwebtoken.lang.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recruit_member")
@Getter
public class RecruitMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_member_id")
    private int id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recruit_board_id")
    private RecruitBoard recruitBoard;

    @Builder
    public RecruitMember(Member member, RecruitBoard recruitBoard) {
        this.member = member;
        this.recruitBoard = recruitBoard;
    }

    /**
     * 연관관계 메소드
     */
    public void initRecruitBoard(RecruitBoard recruitBoard) {
        if(this.recruitBoard != null) {
            this.recruitBoard.getRecruitMembers().remove(this);
        }

        this.recruitBoard = recruitBoard;

        if(recruitBoard != null && !recruitBoard.getRecruitMembers().contains(this)) {
            this.recruitBoard.getRecruitMembers().add(this);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RecruitMember) {
            RecruitMember recruitMember = (RecruitMember) obj;
            if(recruitMember.id == this.id) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
