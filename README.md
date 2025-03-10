## 목차
- [개발 고려 사항](developmentConsiderations.md)
  - [Setter](developmentConsiderations.md#1.-Setter)
  - [Object Mother 패턴](developmentConsiderations.md#2.-Object-Mother-패턴)
  - [Easy Random 라이브러리](developmentConsiderations.md#3.-Easy-Random-라이브러리)
  - [정규화](developmentConsiderations.md#4.-정규화)
  - [결합도](developmentConsiderations.md#5.-결합도)
- [데이터베이스 설계](databaseDesign.md)
  - [데이터베이스 성능](databaseDesign.md#데이터베이스-성능)
  - [인덱스](databaseDesign.md#인덱스)
- [페이지네이션](pagination.md)
    - [오프셋 기반](pagination.md#오프셋-기반의-페이징-구현의-문제)
    - [커서 기반](pagination.md#커서-기반-페이징)
- [타임라인 아키텍처](timelineArchitecture.md)
  - [Pull Model](timelineArchitecture.md#1.-Pull-Model-(Fan-Out-On-Read))
  - [Push Model](timelineArchitecture.md#2.-Push-Model-(Fan-Out-On-Write))
  - [CAP 이론](timelineArchitecture.md#4.-CAP-이론과-타임라인-모델)
- [대용량 시스템 설계](scalability.md)
  - [웹의 기본 아키텍처](scalability.md#1.-웹의-기본-아키텍처)
  - [왜 데이터베이스 병목일까?](scalability.md#2.-왜-데이터베이스-병목일까?)
  - [대용량 시스템 아키텍처](scalability.md#3.-대용량-시스템-아키텍처)

<br/>
<br/>

# 대용량 데이터 및 트래픽 처리를 위한 사이드 프로젝트

이 프로젝트는 **대용량 데이터 처리 및 트래픽 관리**를 이해하기 위해 설계되었습니다.  
회원 관리, 게시물 작성, 타임라인 조회 등의 기능을 포함하며, 성능 최적화를 고려한 설계를 목표로 합니다.

## 기술 스택

- **Framework**: Spring Boot 3.x
- **Database**: MySQL 8.x, JPA (Spring Data JPA)
- **API 문서화**: SpringDoc OpenAPI
- **개발 도구**: Lombok, Spring Boot DevTools
- **테스트**: JUnit, Spring Boot Test, Easy Random

## 기능

### 1. 회원 정보 관리
- 회원 가입 시 **이메일, 닉네임, 생년월일**을 입력받아 저장
- **닉네임 변경**
  - 닉네임은 최대 **10자**까지 가능
  - 변경 이력 조회 기능 제공

### 2. 게시물
- **게시물 등록**
- **게시물 조회**: 3,000,000건의 데이터를 생성 후 **인덱스를 활용한 최적화 적용**
- **내가 쓴 글 캘린더**: 작성일 기준 **게시물 개수 조회 API 제공**

### 3. 팔로우
- 특정 회원 **팔로우 기능 제공**
- 팔로우한 사용자의 **게시물 조회 기능 구현**
