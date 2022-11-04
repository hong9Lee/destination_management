# 여행지 관리 API 프로젝트 #
- 여행지(도시)를 관리하는 REST API 서버 개발
  
**1. 실행방법**  

- 도메인 설정 : resources -> application.yml 의 active(test, dev, prod) 설정
```
spring:
  profiles:
    active: test
    group:
      test:
        - h2
        - common

      dev:
        - h2
        - common

      prod:
        - MySQL
        - common
```

- DB 관련 설정 : application.yml 최하단 on-profile: h2, MySQL의 datasource 설정 
```
---

spring:
  config:
    activate:
      on-profile: h2

  datasource:
    url: jdbc:h2:tcp://localhost/~/travel
    username: sa
    password:
    initialization-mode: always

  h2:
    console:
      enabled: true

---


spring:
  config:
    activate:
      on-profile: MySQL

  datasource:
    url: jdbc:mysql://localhost:3306/travel
    username: root
    password: ****

---
```

- application 실행  
src -> main -> java -> travel -> DestinationManagementApplication Class 실행

  
**2. Entity ERD**   
<img width="1312" alt="스크린샷 2022-11-04 오후 5 01 06" src="https://user-images.githubusercontent.com/94272140/199922559-54ddfa69-609f-46e0-887b-3fa902a97bb9.png">

  
  
**3. API 정의**    

- 도시  
>1. 도시 등록  
method : POST    
url : /city/add  
body : {  
　　String addr_1(필수) - 시  
　　String addr_2(필수) - 도  
　　String cityName(필수) - 지역명   
　　String explanation - 도시 설명    
}  
  
>2. 도시 수정  
method : POST    
url : /city/mod  
body : {  
　　Long cityId(필수) - 수정할 도시 아이디  
　　String explanation - 도시 설명  
　　String addr_1(필수) - 시  
　　String addr_2(필수) - 도  
　　String cityName(필수) - 지역명  
}  
  
>3. 도시 삭제   
method : POST    
url : /city/del  
body : {  
　　Long cityId(필수) - 삭제 도시 아이디  
}  
  
>4. 도시 단건 조회
method : GET  
url : /city/single  
param : cityId, userId  
ex) localhost:8060/travel/single?id=10  
  
>5. 도시 리스트 조회    
method : GET  
url : /city/city-list  
param : userId  
ex) localhost:8060/city/city-list?id=1  
  

- 여행  

>1. 여행 등록  
method : POST    
url : /travel/add  
body : {  
　　String title - 여행 제목  
　　LocalDate startDate(필수) - 여행 시작일(pattern = "yyyy-MM-dd")  
　　LocalDate endDate(필수) - 여행 종료일(pattern = "yyyy-MM-dd")  
　　Long cityId(필수) - city 아이디  
　　Long userId(필수) - user 아이디  
}  
    
>2. 여행 수정  
method : POST    
url : /travel/mod  
body : {  
　　Long travelId(필수) - travel 아이디  
　　String title - 여행 제목  
　　LocalDate startDate(필수) - 여행 시작일(pattern = "yyyy-MM-dd")  
　　LocalDate endDate(필수) - 여행 종료일(pattern = "yyyy-MM-dd")  
　　Long cityId(필수) - city 아이디  
　　Long userId(필수) - user 아이디  
}  
  
>3. 여행 삭제  
method : POST    
url : /travel/del  
body : {  
　　Long travelId(필수) - travel 아이디  
}  
  
>4. 여행 단건 조회  
method : GET    
url : /travel/single  
param : travelId   
ex) localhost:8060/travel/single?id=1  
  
  
**4. Test Case**    
  
API 검증 테스트 : test -> java -> travel -> controller 폴더에 위치합니다.  
단위 테스트 : test -> java -> travel -> service 폴더에 위치합니다.  
  

**5. Test Data**  

- 테스트를 위한 더미 데이터 생성   
위치 : resources -> data.sql  
참고 : 더미 데이터는 TestCase 실행에 영향을 주지 않습니다.  
  
