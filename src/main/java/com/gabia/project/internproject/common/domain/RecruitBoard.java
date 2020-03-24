package com.gabia.project.internproject.common.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "recruit_board")
@EntityListeners(AuditingEntityListener.class)
@Getter
public class RecruitBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recruit_board_id")
    private int id;

    @CreatedDate
    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @NotNull
    @Min(2)
    @Column(name = "max_number")
    private int maxNumber;

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(min=5, max =200, message = "5자 이상 입력해주세요.")
    private String subject;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "recruitBoard", cascade = { CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<RecruitMember> recruitMembers = new ArrayList<>();

    @Builder
    public RecruitBoard(int maxNumber, String subject, Restaurant restaurant) {
        this.maxNumber = maxNumber;
        this.subject = subject;
        this.restaurant = restaurant;
    }

    public long getAttendMemberCount() {
        return recruitMembers.size();
    }

    /**
     * 연관관계 메소드
     */
    public void addRecruitMember(RecruitMember recruitMember) {
        if (!this.recruitMembers.contains(recruitMember)) {
            this.recruitMembers.add(recruitMember);
        }
        recruitMember.initRecruitBoard(this);
    }

}
