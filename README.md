기능 명세서
=============

### **음식점 리스트**

1. 모든 사용자는 음식점 정보를 확인할 수 있다.
2. 모든 사용자는 특정가게의 상세정보를 확인할 수 있다. 
(특정정보 - 가게이름, 위치, 전화번호 등)
3. 모든 사용자는 가게에 대한 임직원들의 평점과 리뷰를 확인할 수 있다.
4. 모든 사용자는 음식점 랭킹 리스트를 확인할 수 있다.
5. 모든 사용자는 카테고리 리스트를 확인 & 음식점 검색 할 수 있다.
6. 모든 사용자는 음식점 이름과 카테고리로 검색을 할 수 있다.

### **리뷰**

1. 임직원들은 리뷰를 작성할 수 있다.
2. 리뷰작성 시 평점을 부여해야한다.
3. 리뷰작성 시 사진을 첨부할 수 있다.

### 게시글

1. 모든 사용자는 게시글 리스트를 확인할 수 있다.
2. 임직원은 게시글 상세 정보를 확인할 수 있다.
3. 임직원은 게시글을 작성할 수 있고, 게시글 모임에 참여할 수 있다.
4. 게시글 작성 시 모임 참여 가능한 최대 인원을 설정해야 한다.
5. 게시글 작성 시 특정 음식점을 선정해야 한다.
6. 임직원은 하루에 하나의 게시글만 참여가 가능하다.
7. 게시글은 참여인원이 0이 되면 자동으로 삭제된다.

### 사용자 페이지

1. 임직원만 사용이 가능하다.
2. 오늘 참여 중인 게시글 정보를 확인할 수 있다.
3. 현재 참여 중인 게시글을 떠날 수 있다.
4. 과거에 참여했던 모집 글 정보를 확인할 수 있다.

<img src="https://user-images.githubusercontent.com/8528707/80446057-c1cc4480-8950-11ea-8fc7-760e33651594.PNG" width="90%"></img>


Restful API
=============

### 음식점
 * 특정 가게 음식점 이미지 GET /api/restaurants/{id}/images
 * 음식점 리스트 GET /api/restaurants
 * 음식점 상세조회 GET /api/restaurants/{id}
 * 이미지 리스트   GET /api/images/restaurants
 
### 리뷰
 * 리뷰 조회
  GET /api/reviews/restaurant
 * 리뷰 쓰기
  POST /api/reviews/restaurant

### 모집 게시판
 * 모집글 조회
  GET /api/boards/recruitments
 * 모집글 상세조회
  GET /api/boards/recruitments/{id}
 * 모집글 쓰기
  POST /api/boards/recruitments
 * 모집글 참여하기
  POST /api/boards/recruitments/{postId}/members/{membersId}
 * 모집글 참여취소
  DELETE /api/boards/recruitments/{postId}/members/{memberId}

### 사용자
 * 사용자 모집글 참여 이력 조회
  GET /api/members/{id}/recruitment
 
### 로그인
* 하이웍스 로그인 Get /hiworks/oauth/callback
* 하이웍스 사용자 조회 GET /hiworks/user
* 로그아웃 GET /logout
