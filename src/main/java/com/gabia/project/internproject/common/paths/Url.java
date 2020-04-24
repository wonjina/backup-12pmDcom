package com.gabia.project.internproject.common.paths;

import lombok.Getter;

@Getter
public enum Url {

    RecruitBoards("/api/boards/recruitment"),
    RecruitBoardInfo("/api/boards/recruitment"),
    RecruitMember("/members"),
    Restaurants("/api/restaurants"),
    RestaurantInfo("/api/restaurants"),
    RemoveRestaurant("/api/restaurants"),
    SearchResCategories("/api/restaurants/categories"),
    AddRestaurant("/api/restaurants"),
    Reviews("/api/reviews/restaurant"),

    /** Front url **/
    FrontMainUrl("http://211.53.209.126:9194"),

    /** Hiworks API **/
    AuthorizationCodeReq("https://api.hiworks.com/open/auth/authform"),
    AccessTokenReq("https://api.hiworks.com/open/auth/accesstoken"),
    RefreshToken("https://api.hiworks.com/open/auth/accesstoken"),
    GetUserInfo("https://api.hiworks.com/user/v2/me"),
    MemberRecord("/api/member/");

    String url;
    Url(String url) {
        this.url = url;
    }
}
