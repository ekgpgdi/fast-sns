# 대용량 데이터 및 트래픽 처리를 위한 사이드 프로젝트

이 프로젝트는 **대용량 데이터 처리 및 트래픽 관리**를 이해하고 최적화하기 위해 설계된 사이드 프로젝트입니다.  
회원 관리, 게시물 작성, 타임라인 조회 등의 기능을 포함하며, 성능 최적화와 동시성 관리에 대한 실습을 목표로 합니다.

<br/>

## 프로젝트 개요

본 프로젝트는 대용량 트래픽 처리 및 효율적인 데이터 관리를 목표로 여러 기술 스택을 활용하여 설계되었습니다.  
주요 기능으로는 **회원 관리**, **게시물 작성 및 조회**, **게시물 좋아요 수 동시성 처리**, **팔로우 기능** 등이 있으며, 이를 통해 시스템의 성능 및 확장성 향상을 도모합니다.

<br/>
<br/>

## 기술 스택

- **Framework**: Spring Boot 3.x
- **Database**: MySQL 8.x, JPA (Spring Data JPA), Redis
- **API 문서화**: SpringDoc OpenAPI
- **개발 도구**: Lombok, Spring Boot DevTools
- **테스트**: JUnit, Spring Boot Test, Easy Random
- **기타**: ExecutorService (동시성 처리), Redis (배치 처리 및 동시성 관리)

<br/>
<br/>

## 기능

### 1. 회원 정보 관리
- **회원 가입**: 이메일, 닉네임, 생년월일을 입력받아 회원 정보를 저장합니다.
- **닉네임 변경**: 최대 10자까지 닉네임을 변경할 수 있으며, 변경 이력 조회 기능이 제공됩니다.

### 2. 게시물
- **게시물 등록**: 사용자가 게시물을 작성할 수 있습니다.
- **게시물 조회**: 3,000,000건 이상의 게시물을 생성하고, **인덱스를 활용한 성능 최적화**를 적용하여 빠르게 조회할 수 있습니다.
- **내가 쓴 글 캘린더**: 사용자가 작성한 게시물의 개수를 날짜별로 조회할 수 있는 API가 제공됩니다.
- **팔로우 게시물 조회**: 특정 사용자가 팔로우한 다른 사용자의 게시물을 조회할 수 있습니다.
- **게시물 좋아요 동시성 관리**:
  - 게시물에 대한 **좋아요 집계 테이블**과 게시물 내 **좋아요 컬럼**을 관리합니다.
  - 좋아요 생성 및 삭제 요청은 **Redis**를 이용하여 관리하며, 1분마다 **배치 업데이트**를 통해 DB와 Redis 데이터를 동기화합니다.

### 3. 팔로우
- **팔로우 기능**: 사용자가 다른 사용자를 팔로우할 수 있습니다.
- **팔로우한 사용자의 게시물 조회**: 팔로우한 사용자가 작성한 게시물을 조회할 수 있습니다.

<br/>
<br/>

## 동시성 관리 및 배치 처리

- **게시물 좋아요 수 동시성 처리**: Redis를 이용하여 좋아요 수를 관리하고, 여러 사용자가 동시에 좋아요를 누르거나 삭제하는 경우에도 정확한 집계를 유지합니다.
- **배치 처리**: 좋아요 수는 일정 시간 간격으로 배치 처리를 통해 DB에 업데이트되어, 실시간으로 데이터를 반영하면서도 부하를 최소화합니다.

<br/>
<br/>

## 학습한 내용

이 프로젝트를 진행하면서 학습한 주요 개념과 기술은 다음과 같습니다:

- [개발 고려 사항](developmentConsiderations.md)
  - [Setter](developmentConsiderations.md#1.-Setter)
  - [Object Mother 패턴](developmentConsiderations.md#2.-Object-Mother-패턴)
  - [Easy Random 라이브러리](developmentConsiderations.md#3.-Easy-Random-라이브러리)
  - [정규화](developmentConsiderations.md#4.-정규화)
  - [결합도](developmentConsiderations.md#5.-결합도)
  - [트랜잭션](developmentConsiderations.md#6.-트랜잭션)
- [데이터베이스 설계](databaseDesign.md)
  - [데이터베이스 성능](databaseDesign.md#데이터베이스-성능)
  - [인덱스](databaseDesign.md#인덱스)
  - [ACID](databaseDesign.md#ACID)
  - [MVCC](databaseDesign.md#MVCC)
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
