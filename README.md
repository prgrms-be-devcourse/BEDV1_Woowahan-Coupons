# BEDV1_Woowahan-Coupons

<div align="left">
<img src="https://img.shields.io/badge/-Spring Boot-6DB33F?style=flat&logo=SpringBoot&logoColor=white">
<img src="https://img.shields.io/badge/-Gradle-02303A?style=flat&logo=Gradle">
<img src="https://img.shields.io/badge/-Java-007396?style=flat&logo=Java">
<img src="https://img.shields.io/badge/-JPA-FF3621?style=flat&logo=Databricks&logoColor=white">
<img src="https://img.shields.io/badge/-MySQL-4479A1?style=flat&logo=MySQL&logoColor=white">
<img src="https://img.shields.io/badge/-AWS EC2, S3, Code Deploy-232F3E?style=flat&logo=AmazonAWS">
<img src="https://img.shields.io/badge/-Jira Software-0052CC?style=flat&logo=JiraSoftware">
<img src="https://img.shields.io/badge/-GitHub Actions-2088FF?style=flat&logo=GitHubActions&logoColor=white">
<img src="https://img.shields.io/badge/-Sonar Cloud-F3702A?style=flat&logo=SonarCloud&logoColor=white">
</div>
<br><br>

# 📣 쿠폰의 민족 - 우아한 쿠폰들

안녕하세요. 프로그래머스 데브코스: 클라우드 기반 백엔드 엔지니어링 과정을 듣고 있는 쿠폰의 민족팀입니다.

지난 10월 22일, 팀이 구성된 이후 맡게 된 첫 번째 프로젝트 만큼 나름 의욕 넘치게 진행됐던 개발 진행과정에서의 경험을 공유합니다.
<br><br><br><br>

# 🎫 Woowahan-Coupons

저희 팀의 프로젝트인 ‘**우아한 쿠폰들**‘은 배달의 민족의 쿠폰 서비스를 클론코딩 한 프로젝트입니다.

![image](https://user-images.githubusercontent.com/36220595/140631449-5556dc84-4cf0-490f-ac4c-0b98b4afdee1.png)
<br><br><br><br>
# 🐮 역할

- **스크럼 마스터(SM)**
    - 나상원
- **프로덕트 오너(PO)**
    - 한맹희
- **개발자(Developers)**
    - 나상원, 한맹희, 이주오
<br><br><br><br>

# **🚀 기술스택**

### **개발 환경**

- Java 11
- Spring Boot 2.5.6
- JPA
- AWS RDS(MySQL8)
- AWS EC2, S3, Code Deploy
- gradle 7
- Jacoco
<br><br>

### **협업 툴**

- Notion
- Slack
- Jira
<br><br>

### **기타**

- ERDCloud
- Postman
- Rest Docs
- Dbeaver, Mysql WorkBench
- Github Action
- sonar cloud
<br><br>

## 패키지 구조

```bash
**Woowahan-Coupons**
├─docs
│  └─asciidoc
├─main
│  ├─java
│  │  └─com
│  │      └─coumin
│  │          └─woowahancoupons
│  │              ├─config
│  │              ├─coupon
│  │              │  ├─controller
│  │              │  ├─converter
│  │              │  ├─dto
│  │              │  ├─service
│  │              │  └─validator
│  │              │      └─annotation
│  │              ├─customer
│  │              │  ├─controller
│  │              │  └─service
│  │              ├─domain
│  │              │  ├─coupon
│  │              │  ├─customer
│  │              │  └─store
│  │              ├─global
│  │              │  ├─error
│  │              │  └─exception
│  │              └─store
│  │                  ├─controller
│  │                  └─service
│  └─resources
└─test
    └─java
        └─com
            └─coumin
                └─woowahancoupons
                    ├─coupon
                    │  ├─controller
                    │  ├─document
                    │  ├─factory
                    │  └─service
                    ├─domain
                    │  └─coupon
                    └─store
                        ├─controller
                        └─service
```
<br><br><br><br>

# 🎯 프로젝트 목표

![Untitled 1](https://user-images.githubusercontent.com/36220595/140631419-f057fc8e-d2c9-431e-87f2-85204cb5c3e8.png)

### **같은 분야의 개발자끼리의 협업**

- 지라등의 협업툴로 애자일 프로세스 경험 해보기
- git flow 브랜치 전략을 사용하여 하나의 프로젝트 코드를 여러명이서 효율적으로 관리 해보기
- 컨벤션을 정해서 맞춰가며 프로젝트 진행 해보기
    - 깃허브 컨벤션
    - 코드 컨벤션
<br><br>

### **실제 상황이라고 가정하고 무조건 2주의 데드라인을 지킬 수 있도록 한다.**

- 처음부터 많은걸 하려고 하지 않는다.
- 애자일 처럼 처음부터 동작가능한 최소 기능을 만들고 여유가 있을때 살을 붙인다.
    - 2주를 1주씩 스프린트를 두개로 나누어서 첫 스프린트 때 최소 기능을 가진 애플리케이션을 선 배포
<br><br>

### **평소 토이 프로젝트에서 경험하지 못해본 기술을 하나 이상 다룬다.**

- 선착순 N명을 처리하기 위한 트랜잭션 다루기
- Jacoco 사용해서 테스트 커버리지 80% 이상 달성하기
<br><br>

### **쿠폰 도메인 경험**

- 쿠폰 번호는 어떻게 생성해야 하지?
- 쿠폰 테이블은 어떻게 설계 할까?
- 배민을 예로 들었을때 쿠폰의 발급 주체는 누구고 쿠폰의 사용 대상은?
- 쿠폰을 사용할때의 다양한 조건은 어떤게 있지?
<br><br>

### **문서화와 배포**

- 내 API를 사용하게 될 클라이언트에게 어떻게 하면 보기 좋은 문서를 보여줄 수 있을까?
- AWS Cloud 사용 및 CI/CD 적용해보기
<br><br>
<br><br>

# 🤡 요구사항 분석

배달의 민족 앱을 분석해 보면서 도메인과 기능을 도출하고 쿠폰 서비스에 대한 이해를 하고자 했습니다.

- 쿠폰 발급 주체
    - 쿠폰 어드민
    - 매장 어드민
- 쿠폰 scope 주체
    - 배민 자체 (ex 배민 첫 주문 만원 할인, B마트 추석맞이 할인 쿠폰)
    - 음식점 브랜드  → brand (ex 네네치킨 브랜드관 쿠폰)
    - 음식점 → store (ex 맹호수제돈까스 부천점의 최소 주문금액 별 할인 쿠폰)
- 쿠폰 발급 대상
    - 음식을 주문하는 배민 모바일 앱 사용자
- 쿠폰 발급 방식
    - 쿠폰 번호 등록
    - 클릭으로 바로 등록
- 쿠폰 사용 조건
    - 최소 주문 금액
    - 사용 기간
        - 발급 후 N일 동안 사용
        - yyyy.MM.dd ~ yyyy.MMdd 까지
    - 첫 주문
    - ~~물건 N개 이상 구매 (라이브 쇼핑)~~
    - ~~N개월간 현대카드 사용하지 않음~~
    - ~~포장 or 배달~~
    - 사용후 특정 시간 후 재발급 가능
    - 사람 별 제한
    - 수량 제한(선착순 이벤트)
- 쿠폰 종류
    - 할인 쿠폰
        - 고정 금액 할인 (ex 천원 할인 쿠폰)
        - 퍼센트 할인 (ex 20% 할인 쿠폰)
    - ~~무료 쿠폰~~
        - ~~배달비 무료 쿠폰~~
    - ~~랜덤쿠폰~~
<br><br>


### 제품 사용자 정의

- **사용자** - 쿠폰을 발급받아서 실제 사용하는 주체
- **매장(사장님), 브랜드** - 음식점과 그 프렌차이즈로서, 쿠폰을 생성할 수 있습니다.
- **쿠폰** **운영자(배민)** - 사내에서 쿠폰 어드민 페이지를 통해 쿠폰 서비스를 운영합니다.
<br><br>

### 사용자 스토리

![Untitled 2](https://user-images.githubusercontent.com/36220595/140631420-0493580f-6904-4dc8-a574-65a710aed4a6.png)

팀 노션에 작성한 초기 사용자 스토리의 일부
<br><br>

## 유스케이스

![Untitled 3](https://user-images.githubusercontent.com/36220595/140631421-59d4cab6-e396-4fb5-825e-7bbc83b0ae8f.png)
<br><br>

## 도메인 객체

유스케이스를 통해 드러난 도메인의 개념들을 객체와 상태 등으로 점차 모델링 해 나갔습니다.

![Untitled 4](https://user-images.githubusercontent.com/36220595/140631422-87a3d1cf-e01f-49d9-8190-e3f4ea4d6433.png)
![Untitled 5](https://user-images.githubusercontent.com/36220595/140631423-b6225140-869c-4641-9eea-2a25a7866f4d.png)
<br><br>
<br><br>

# 💌 프로젝트 컨벤션

## 스크럼 및 코어타임

- 데일리 스크럼 14시
- 프로젝트 코어타임 14 ~ 19시
<br><br>

## **Git Commit Message Type**

![Untitled 6](https://user-images.githubusercontent.com/36220595/140631424-24896a5c-8a35-4886-b75a-3df17870c7cf.png)

```bash
1- ⭐ feat : 새로운 기능에 대한 커밋
2- ⚙️ chore : 그 외 자잘한 수정에 대한 커밋
3- 🐞 fix : 버그 수정에 대한 커밋
4- 📖 docs : 문서 수정에 대한 커밋
5- 💅 style : 코드 스타일 혹은 포맷 등에 관한 커밋
6- ♻️ refactor : 코드 리팩토링에 대한 커밋
7- 🚦 test : 테스트 코드 수정에 대한 커밋
8- 🚀 CI : CI/CD
9- 🔖 Release : 제품 출시
10- 🎉 init : 최초 커밋
11- 🛠️ Config : 환경설정에 대한 커밋
12- 🦔 Revert : 리버트
```
<br><br>

## 깃허브

- 브랜치 전략은 git flow를 사용했습니다.
- 깃허브는 원본 저장소를 fork 하는 방식으로 사용했습니다.
- PR로만 merge되게 하였습니다.
- 리뷰 approval이 2개 이상이여야만 merge 승인이 나도록 했습니다.
    
    ![https://user-images.githubusercontent.com/49011919/140630372-2f07e682-ea73-4137-b939-e8c758c70d47.PNG](https://user-images.githubusercontent.com/49011919/140630372-2f07e682-ea73-4137-b939-e8c758c70d47.PNG)
    
- 테스트를 모두 통과해야 merge가 가능하도록 설정했습니다.
    
    ![https://user-images.githubusercontent.com/49011919/140630396-fd4bcbfd-10a6-41f3-9749-22a980249b5b.PNG](https://user-images.githubusercontent.com/49011919/140630396-fd4bcbfd-10a6-41f3-9749-22a980249b5b.PNG)
    
- 지라 1티켓 = 1PR 원칙 (PR안에 커밋 수는 제한을 두지 않았습니다.)
- 깃허브 - 지라 연동 기능을 사용했고 PR 단위로 적용했습니다.

![https://user-images.githubusercontent.com/49011919/140629596-6ed896ed-0397-485a-95b6-6f3e14a62755.png](https://user-images.githubusercontent.com/49011919/140629596-6ed896ed-0397-485a-95b6-6f3e14a62755.png)
<br><br>

## Jira Software

### 이슈 생성

- 백로그로 추가된 이슈는 단위를 작게 쪼개서 하위작업으로 추가하도록 했습니다.

### 자동화

- 깃허브-지라 연동 스마트 커밋 기능 사용
    
    ![image](https://user-images.githubusercontent.com/36220595/140631478-111f5a1b-893a-4813-8580-ab1e0d60779e.png)
    
- 에픽의 하위 작업이 완료되면, 상위 작업을 완료로 이동
- 깃허브 PR merge시 이슈 자동 완료 처리
![image](https://user-images.githubusercontent.com/36220595/140631490-0e789aa4-c766-4253-b01f-be0663771ec8.png)
<br><br>

## 코드

코드 컨벤션은 **구글의 자바 컨벤션**을 따르기로 결정했습니다.

또한 `sonar lint`를 적용해서 리뷰를 받기 전에 컨벤션에 대한 검사를 수행하도록 했습니다.
<br><br>

### Lombok

롬복을 사용할때 발생할 수 있는 사이드 이펙트를 최소화 하기 위해 다음의 룰을 정했습니다.

- @setter 금지
- @Getter 사용 가능
- @NoArgsConstructor 사용 가능 그외 생성자 관련 Annotation 사용 금지
- 선택적 @Builder 사용가능
<br><br>

### **DTO**

1. dto 클래스 네이밍은 surffix로 Dto 붙이기
2. nested dto 클래스의 필드는 dto 사용 금지
3. collection 형태이면 wrapper로 감싸서 일급 컬렉션 만들기
<br><br><br><br>

# 😈 CI/CD

### Github Action

![image](https://user-images.githubusercontent.com/36220595/140631504-5ae2e61f-a25d-4cf9-88bd-27c54a68fcc6.png)

![image](https://user-images.githubusercontent.com/36220595/140631508-9bf28ab9-308e-41e0-b91a-6842cc123b8d.png)
- java ci with gradle : ci 및 빌드 확인
- Sonar-build : push 된 commit  코드스멜 등 코드 분석
- deploy : 배포
<br><br>

## AWS(EC2, S3, Code Deploy)

![image](https://user-images.githubusercontent.com/36220595/140631512-25e208e9-4745-4e4a-8914-8fd3c43b272e.png)
<br><br><br><br>

# Test

- 되도록이면 PR할때 해당 코드에 대한 테스트를 포함하도록 했습니다.
- jacoco를 이용해서 코드의 커버리지 확인과 커버리지 기준을 80%로 제한하여 만족하지 않으면 빌드가 되지 않도록 했습니다.

![image](https://user-images.githubusercontent.com/36220595/140631519-ae7e7ec4-ae13-4195-9722-dac80efad0a5.png)
<br><br><br><br>

# API Docs

- [http://ec2-3-36-59-242.ap-northeast-2.compute.amazonaws.com:8080/docs/index.html](http://ec2-3-36-59-242.ap-northeast-2.compute.amazonaws.com:8080/docs/index.html)
<br><br><br><br>

# 회고

- [회고 링크](https://backend-devcourse.notion.site/59ba830a27bf430984b018213d1e07c0)