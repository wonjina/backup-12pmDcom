package com.gabia.project.internproject.controller.oauth;

import com.gabia.project.internproject.common.exception.UnknownUserException;
import com.gabia.project.internproject.common.helper.Notification;
import com.gabia.project.internproject.common.paths.Url;
import com.gabia.project.internproject.common.util.CustomCookies;
import com.gabia.project.internproject.controller.dto.ResponseDto;
import com.gabia.project.internproject.security.properties.JwtCookieProperties;
import com.gabia.project.internproject.service.ouath.OauthService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;
    private UriComponents uri;
    private final CustomCookies customCookies;
    private final JwtCookieProperties jwtCookieProperties;

    /**
     * Hiworks auth_code redirect 되는 장소
     * @param auth_code
     * @param response
     * @return
     */
    @GetMapping("/hiworks/oauth/callback")
    public RedirectView loginHiworks(@RequestParam String auth_code, HttpServletResponse response) {
        String jwtToken = oauthService.generationHiworksJwtToken(auth_code);
        Cookie cookie = customCookies.createCookie(jwtCookieProperties, jwtToken);
        response.addCookie(cookie);
        System.out.println("callback!! : token->"+jwtToken);
        System.out.println(cookie.getValue()+","+cookie.getDomain()+","+cookie.getPath()+","+cookie.getName());

        uri = UriComponentsBuilder.fromHttpUrl(Url.FrontMainUrl.getUrl()).build();
        return new RedirectView(uri.toUriString());
    }

    @GetMapping(value = "/hiworks/user", produces = "application/json; charset=utf8")
    public ResponseDto getUserInfo(@CookieValue(value="token", required=false, defaultValue="") String token)
            throws UnknownUserException, JwtException {
        return new ResponseDto(oauthService.searchUserInfo(token));
    }

    @GetMapping("/logout")
    public ResponseDto logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = WebUtils.getCookie(request, "token");
        if(cookie != null) {
            response.addCookie(oauthService.logout(cookie));
        }
        return new ResponseDto(Notification.SUCCESS_DELETE.getMsg());
    }
}
