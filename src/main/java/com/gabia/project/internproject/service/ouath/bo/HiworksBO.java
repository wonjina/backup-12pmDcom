package com.gabia.project.internproject.service.ouath.bo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.project.internproject.security.properties.HiworksProperties;
import com.gabia.project.internproject.common.paths.Url;
import com.gabia.project.internproject.service.ouath.vo.AccessTokenVO;
import com.gabia.project.internproject.service.ouath.vo.HiworksUserVO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class HiworksBO {

    @Value("${spring.ouath.hiworks.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.ouath.hiworks.accessTokenUri}")
    private String accessTokenUri;

    @Value("${spring.ouath.hiworks.accessTokenUri}")
    private String authorizationCodeUri;

    private final HiworksProperties hiworksProperties;

    private String ACCESS_TOKEN;
    private RestTemplate restTemplate = new RestTemplate();
    private UriComponents uri;
    private ObjectMapper om = new ObjectMapper();

    public AccessTokenVO createAccessToken(String code) {
        hiworksProperties.initAuthCode(code);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.setAll(om.convertValue(hiworksProperties, new TypeReference<Map<String, String>>() {}));

        uri = UriComponentsBuilder.fromHttpUrl(Url.AccessTokenReq.getUrl()).build();

        AccessTokenVO accessTokenVO = restTemplate.postForObject(uri.toUriString(),params, AccessTokenVO.class);
        ACCESS_TOKEN = accessTokenVO.getData().getAccess_token();

        return accessTokenVO;
    }

    public HiworksUserVO getUserInfo() {
        uri = UriComponentsBuilder.fromHttpUrl(Url.GetUserInfo.getUrl()).build();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(this.ACCESS_TOKEN);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity requestEntity = new HttpEntity(headers);
        ResponseEntity<HiworksUserVO> userRes =
                restTemplate.exchange(uri.toUriString(), HttpMethod.GET, requestEntity, HiworksUserVO.class);

        return userRes.getBody();
    }
}
