package com.gabia.project.internproject.common.util.mapper;
import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.remoteService.googlePlaceService.dao.GoogleReview;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "department", ignore = true)
    @Mapping(target = "employeeNumber",ignore = true)
    @Mapping(source = "author_name", target = "name")
    Member toEntity(GoogleReview googleReview);
}