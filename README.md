## 목차
- [개발 고려 사항](development_considerations.md)
- [데이터베이스 설계](database_design.md)
- [페이지네이션](pagination.md)
- [타임라인 아키텍처](timeline_architecture.md)
- [대용량 시스템 설계](scalability.md)

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
