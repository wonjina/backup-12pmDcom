package com.gabia.project.internproject.service.ouath;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabia.project.internproject.common.domain.Member;
import com.gabia.project.internproject.common.exception.UnknownUserException;
import com.gabia.project.internproject.common.helper.Notification;
import com.gabia.project.internproject.common.util.CustomCookies;
import com.gabia.project.internproject.controller.oauth.dto.UserInfo;
import com.gabia.project.internproject.repository.MemberRepository;
import com.gabia.project.internproject.security.properties.HiworksHeaderProperties;
import com.gabia.project.internproject.service.ouath.bo.HiworksBO;
import com.gabia.project.internproject.security.util.JwtTokenProvider;
import com.gabia.project.internproject.service.ouath.vo.AccessTokenVO;
import com.gabia.project.internproject.service.ouath.vo.HiworksUserVO;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class OauthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final HiworksBO hiworksBO;
    private final MemberRepository memberRepository;
    private final CustomCookies customCookies;
    private final HiworksHeaderProperties hiworksHeaderProperties;
    private ObjectMapper om = new ObjectMapper();

    /**
     * Jwt token 생성
     * @param auth_code
     * @return
     */
    public String generationHiworksJwtToken(String auth_code) {
        /** Access Token 발급 및 Get user info **/
        AccessTokenVO accessTokenVO = hiworksBO.createAccessToken(auth_code);
        HiworksUserVO userInfo = hiworksBO.getUserInfo();
        userInfo.initOfficeNumber(accessTokenVO.getData().getOffice_no());

        /** user가 존재하는지 확인 **/
        if(userInfo != null && StringUtils.hasText(userInfo.getNo())) {
            memberRepository.findMemberByEmployeeNumber(userInfo.getNo())
                    .ifPresent(m -> userInfo.initMemberId(m.getId()));
        }

        /** 등록되지 않은 유저인 경우 저장 **/
        if(userInfo.getMemberId() == null) {
            Member member =  memberRepository.save(Member.builder()
                    .employeeNumber(userInfo.getNo()+"")
                    .department(accessTokenVO.getData().getOffice_no())
                    .name(userInfo.getName())
                    .build());
            userInfo.initMemberId(member.getId());
        }

        /** jwt 토큰에 주입할 데이터 셋팅 **/
        Map<String, Object> headerMap = om.convertValue(hiworksHeaderProperties, new TypeReference<HashMap<String, Object>>() {});
        Map<String, Object> map = om.convertValue(userInfo, Map.class);

        return jwtTokenProvider.generateHiworksJwtToken(headerMap, map);
    }

    /**
     * Get 로그인 user 정보 from jwt Token
     * @param token
     * @return
     */
    public UserInfo searchUserInfo(String token) throws JwtException, IllegalArgumentException {
        if(!StringUtils.hasText(token)) {
            throw new UnknownUserException(Notification.UNKNOWN_TOKEN.getMsg());
        }
        return new UserInfo(om.convertValue(jwtTokenProvider.getJwtValue(token), HiworksUserVO.class));
    }

    /**
     * Token cookie 삭제
     * @param cookie
     * @return
     */
    public Cookie logout(Cookie cookie) {
        if(cookie != null) {
            return customCookies.clearCookie(cookie);
        }
        return null;
    }
}
