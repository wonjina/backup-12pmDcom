package com.gabia.project.internproject.remoteService.googlePlaceService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.domain.Restaurant;
import com.gabia.project.internproject.common.domain.RestaurantImg;
import com.gabia.project.internproject.common.domain.Review;
import com.gabia.project.internproject.common.util.mapper.RestaurantMapper;
import com.gabia.project.internproject.common.util.mapper.ReviewMapper;
import com.gabia.project.internproject.remoteService.googlePlaceService.bo.GooglePlacesApiBO;
import com.gabia.project.internproject.remoteService.googlePlaceService.dao.GoogleDetailDao;
import com.gabia.project.internproject.remoteService.googlePlaceService.dao.GooglePlaceDao;
import com.gabia.project.internproject.remoteService.googlePlaceService.dao.GoogleReview;
import com.gabia.project.internproject.remoteService.googlePlaceService.dao.Photos;
import com.gabia.project.internproject.remoteService.properties.GooglePlaceDetailProperty;
import com.gabia.project.internproject.remoteService.properties.GooglePlaceParamsProperty;
import com.gabia.project.internproject.remoteService.properties.GooglePlacePhotoProperty;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.repository.RestaurantImgRepository;
import com.gabia.project.internproject.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GooglePlaceService {

    private final RestaurantMapper restaurantMapper;
    private final ReviewMapper reviewMapper;

    private final RestaurantRepository restaurantRepository;
    private final RestaurantImgRepository restaurantImgRepository;
    private final MemberRepository memberRepository;
    private final JdbcTemplate jdbcTemplate;

    private ObjectMapper om = new ObjectMapper();

    @PersistenceContext
    private EntityManager em;

    @Value("${custom.path.upload.source}")
    private String uploadSourcePath;

    private final GooglePlacesApiBO googlePlacesApiBO;

    public void getRestaurantsInfo() throws ParseException, InterruptedException, IOException {
        List<Restaurant> dbResList = restaurantRepository.findAll();
        List<Member> dbMemberList = memberRepository.findAll();
        Map<String, Member> dbMemMap = dbMemberList.stream()
                .filter(distinctByKey(Member::getName))
                .collect(Collectors.toMap(Member::getName, member -> member));

        Map<String, Restaurant> dbResMap = dbResList.stream()
                .filter(distinctByKey(Restaurant::getName))
                .collect(Collectors.toMap(Restaurant::getName, restaurant -> restaurant));

        do {
            //search API call & dto 변환
            String searchApiResult = googlePlacesApiBO.getPlacesSearchApi();
            List<GooglePlaceDao> gpdao = om.convertValue(googlePlacesApiBO.parsingPlaceSearchResult(searchApiResult), new TypeReference<List<GooglePlaceDao>>(){});

            //detail api call & dto 변환
            List<GoogleDetailDao> detailDaos = gpdao.stream()
                    .map(gp -> {
                        try {
                            String detailApiResult = googlePlacesApiBO.getPlaceDetailApi(gp.getPlace_id());
                            return om.convertValue(googlePlacesApiBO.parsingPlaceDetailResult(detailApiResult), GoogleDetailDao.class);
                        } catch (ParseException e) {
                            return null;
                        }
                    }).collect(Collectors.toList());

            //detail API 결과 dto를 엔티티로(가게, 리뷰, 멤버) 변환 및 저장
            if(detailDaos != null && detailDaos.size() > 0) {

                // saveAll 하기 위해 list에 엔티티 저장
                List<Review> reviews = new ArrayList<>();
                Map<String, Member> members = new HashMap<>();
                List<Restaurant> restaurants = new ArrayList<>();
                List<RestaurantImg> restaurantImgs = new ArrayList<>();

                // 가게 갯수만큼 for
                for (GoogleDetailDao detailDao : detailDaos) {
                    // 중복된 가게는 제외
                    if(dbResMap.containsKey(detailDao.getName())) {
                        continue;
                    }
                    // 가게 엔티티 생성
                    restaurants.add(0, restaurantMapper.toEntity(detailDao));
                    dbResMap.put(detailDao.getName(), restaurants.get(0));

                    // Place Img get API call & save local folder
                    if(detailDao.getPhotos() != null && detailDao.getPhotos().size() > 0) {
                        for (Photos photo : detailDao.getPhotos()) {
                            String fileName = googlePlacesApiBO.downloadRestaurantImgApi(detailDao.getPlace_id(), photo.getPhoto_reference());
                            if(StringUtils.hasText(fileName)) {
                                restaurantImgs.add(RestaurantImg.builder()
                                        .restaurant(restaurants.get(0))
                                        .url(uploadSourcePath + fileName)
                                        .build());
                            }
                        }
                    }

                    // 가게에 리뷰가 있을 경우 가게의 리뷰 엔티티 리스트, 작성자 엔티티 리스트 생성
                    if(detailDao != null && detailDao.getReviews() != null && detailDao.getReviews().size() > 0) {
                        Member tmpMember = null;

                        //리뷰 갯수만큼 for
                        for (GoogleReview googleReview : detailDao.getReviews()) {
                            // 작성자가 있을 경우
                            if(StringUtils.hasText(googleReview.getAuthor_name())) {
                                reviews.add(0, reviewMapper.toEntity(googleReview));

                                // DB에 존재하는 작성자일 경우 db 엔티티 사용
                                if(dbMemMap.containsKey(googleReview.getAuthor_name())) {
                                    tmpMember = dbMemMap.get(googleReview.getAuthor_name());
                                } else if (!members.containsKey(googleReview.getAuthor_name())) {
                                    // db에 존재하지 않고 작성지 엔티티 리스트에 이미 포함되있지 않다면 멤버 엔티티 생성
                                    members.put(googleReview.getAuthor_name(), Member.of(googleReview.getAuthor_name()));
                                    dbMemMap.put(googleReview.getAuthor_name(), members.get(0));
                                    tmpMember = members.get(googleReview.getAuthor_name());
                                } else {
                                    tmpMember = members.get(googleReview.getAuthor_name());
                                }
                                if(tmpMember == null) {
                                    reviews.remove(0);
                                    continue;
                                }
                                reviews.get(0).setMember(tmpMember);
                                restaurants.get(0).addReview(reviews.get(0));
                            }
                        }
                        restaurants.get(0).updateRating();
                        restaurants.get(0).updateReviewsAmount();
                    }


                    if(members.size() > 0) {
                        memberRepository.saveAll(new ArrayList<>(members.values()));
                    }
                    if(restaurants.size() > 0) {
                        restaurantRepository.saveAll(restaurants);
                    }
                    if(restaurantImgs.size() > 0) {
                        restaurantImgRepository.saveAll(restaurantImgs);
                    }
                    em.flush();
                    Thread.sleep(5000);
                }
            }
        } while(StringUtils.hasText(googlePlacesApiBO.getNext_page_token()));
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
