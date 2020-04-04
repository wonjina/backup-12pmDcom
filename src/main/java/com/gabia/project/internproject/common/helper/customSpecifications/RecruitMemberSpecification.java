package com.gabia.project.internproject.common.helper.customSpecifications;

import com.gabia.project.internproject.common.domain.RecruitBoard;
import com.gabia.project.internproject.common.domain.RecruitBoard_;
import com.gabia.project.internproject.common.domain.RecruitMember;
import com.gabia.project.internproject.common.domain.RecruitMember_;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RecruitMemberSpecification {

    public static <Y extends Comparable<? super Y>> Specification<RecruitMember> greaterThanDateTime(LocalDateTime dateTime) {
        if(dateTime == null) {
            return null;
        }
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.join(RecruitMember_.RECRUIT_BOARD)
                                                                .get(RecruitBoard_.DATE_TIME), dateTime);
    }

    public static Specification<RecruitMember> equalMember(Integer value) {
        if(value == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get(RecruitMember_.MEMBER), value);
    }

    public static Specification<RecruitMember> fetchMember() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList();
            root.fetch(RecruitMember_.MEMBER);
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<RecruitMember> equalRecruitBoardNMember(List<Integer> value) {
        if(value == null || value.size() == 0) {
            return null;
        }
        return (root, query, cb) -> root.get(RecruitMember_.RECRUIT_BOARD).in(value);
    }

}
