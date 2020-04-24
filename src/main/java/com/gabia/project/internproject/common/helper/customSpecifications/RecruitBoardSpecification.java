package com.gabia.project.internproject.common.helper.customSpecifications;

import com.gabia.project.internproject.common.domain.Member_;
import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitBoard_;
import com.gabia.project.internproject.common.domain.RecruitMember_;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;

public class RecruitBoardSpecification {

    public static <Y extends Comparable<? super Y>> Specification<RecruitBoard> greaterThanDateTime(LocalDateTime dateTime) {
        if(dateTime == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(RecruitBoard_.DATE_TIME), dateTime);
    }

    public static <Y extends Comparable<? super Y>> Specification<RecruitBoard> lessThanDateTime(LocalDateTime dateTime) {
        if(dateTime == null) {
            return null;
        }
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(RecruitBoard_.DATE_TIME), dateTime);
    }

    public static <Y extends Comparable<? super Y>> Specification<RecruitBoard> equalRecMember(Integer memId) {
        if(memId == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.join(RecruitBoard_.RECRUIT_MEMBERS).get(RecruitMember_.MEMBER), memId);
    }

    public static Specification<RecruitBoard> equalRecruitBoard(List<Integer> value) {
        if(value == null || value.size() == 0) {
            return null;
        }
        return (root, query, cb) -> root.get(RecruitBoard_.ID).in(value);
    }

}

