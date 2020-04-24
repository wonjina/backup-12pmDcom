package com.gabia.project.internproject.remoteService.googlePlaceService.bo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.project.internproject.remoteService.properties.GooglePlaceDetailProperty;
import com.gabia.project.internproject.remoteService.properties.GooglePlaceParamsProperty;
import com.gabia.project.internproject.remoteService.properties.GooglePlacePhotoProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Getter
public class GooglePlacesApiBO {

    private final RestTemplate googlePlaceRestTemplate;

    private final GooglePlaceParamsProperty googlePlaceParamsProperty;
    private final GooglePlaceDetailProperty googlePlaceDetailProperty;
    private final GooglePlacePhotoProperty googlePlacePhotoProperty;

    private MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    private ObjectMapper om = new ObjectMapper();
    private UriComponents uri;

    @Value("${google.place.nearbysearch.uri}")
    private String GOOGLE_PLACE_API_URI;

    @Value("${google.place.detail.uri}")
    private String GOOGLE_PLACE_DETAIL_API_URI;

    @Value("${custom.path.upload-images}")
    private String uploadImgPath;

    @Value("${google.place.photo.api.uri}")
    private String GOOGLE_PLACE_PHOTO_API_URI;

    private String next_page_token;


    public String downloadRestaurantImgApi(String prefixFileName, String photoReference) {
        googlePlacePhotoProperty.initPhotoReference(photoReference);
        params.clear();
        params.setAll(om.convertValue(googlePlacePhotoProperty, new TypeReference<Map<String, String>>() {}));
        uri = UriComponentsBuilder.fromHttpUrl(GOOGLE_PLACE_PHOTO_API_URI)
                .queryParams(params)
                .build();

        byte[] imgResult = googlePlaceRestTemplate.getForObject(uri.toUri(), byte[].class);

        String fileName = prefixFileName + "-" + UUID.randomUUID().toString() +".jpg";
        File imageFile = new File(uploadImgPath, fileName);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(imageFile);
            BufferedOutputStream os = new BufferedOutputStream(fileOutputStream);
            os.write(imgResult);
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return fileName;
    }

    public String getPlaceDetailApi(String placeId) {
        params.clear();
        googlePlaceDetailProperty.initPlaceId(placeId);
        params.setAll(om.convertValue(googlePlaceDetailProperty, new TypeReference<Map<String, String>>() {}));
        uri = UriComponentsBuilder.fromHttpUrl(GOOGLE_PLACE_DETAIL_API_URI).queryParams(params).build();
        return googlePlaceRestTemplate.getForObject(uri.toUri(), String.class);

    }

    public String getPlacesSearchApi() {
        params.clear();
        params.setAll(om.convertValue(googlePlaceParamsProperty, new TypeReference<Map<String, String>>(){}));
        if(StringUtils.hasText(next_page_token)) {
            params.set("next_page_token", next_page_token);
        }
        uri = UriComponentsBuilder.fromHttpUrl(GOOGLE_PLACE_API_URI).queryParams(params).build();
        return googlePlaceRestTemplate.getForObject(uri.toUri(), String.class);
    }

    /**
     * google places search api parser
     * @param result
     * @return
     * @throws ParseException
     */
    public ArrayList<LinkedHashMap<String, String>> parsingPlaceSearchResult(String result) throws ParseException {
        JSONParser parser = new JSONParser(result);
        LinkedHashMap<String, Object> o = parser.parseObject();
        if(o.containsKey("next_page_token")) {
            if(o.get("next_page_token") instanceof String) {
                next_page_token = o.get("next_page_token").toString();
            }
        } else {
            next_page_token = null;
        }

        return (ArrayList<LinkedHashMap<String, String>>) o.get("results");
    }

    /**
     * google places detail api parser
     * @param result
     * @return
     * @throws ParseException
     */
    public LinkedHashMap<String, String> parsingPlaceDetailResult(String result) throws ParseException {
        JSONParser parser = new JSONParser(result);
        LinkedHashMap<String, Object> o = parser.parseObject();
        return (LinkedHashMap<String, String>) o.get("result");
    }
}