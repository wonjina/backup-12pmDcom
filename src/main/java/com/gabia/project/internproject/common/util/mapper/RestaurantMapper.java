package com.gabia.project.internproject.common.util.mapper;

import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.helper.PatternType;
import com.gabia.project.internproject.remoteService.googlePlaceService.dao.GoogleDetailDao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    @Mapping(source = "formatted_address", target = "loadAddress")
    @Mapping(source = "formatted_phone_number", target = "cellNumber", qualifiedByName = "stringToDateTime")
    @Mapping(source = "geometry.location.lng", target = "locationY")
    @Mapping(source = "geometry.location.lat", target = "locationX")
    @Mapping(target="reviewAmount", ignore = true)
    @Mapping(target="rating", ignore = true)
    @Mapping(target="zipCode", ignore = true)
    Restaurant toEntity(GoogleDetailDao googlePlaceDao);


    @Named("stringToDateTime")
    public static String replaceSpecialChar(String cellNumber) {
        if(!StringUtils.hasText(cellNumber)) {
            return null;
        }
        return cellNumber.replaceAll(PatternType.ONLY_NUMBER.getRex(),"");
    }

}
