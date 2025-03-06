## 1. 대용량 시스템에 대한 이해 

## 1. 웹의 기본 아키텍처

### 초기 웹 아키텍처
과거의 웹 아키텍처는 다음과 같은 형태로 단순하게 구성되었음:

```
클라이언트 → 웹 서버 → 레포지토리
```

- **클라이언트**: 브라우저 등을 통해 요청을 보냄
- **웹 서버**: 요청을 받아 정적인 파일을 제공
- **레포지토리**: 데이터베이스 등 데이터 저장소

하지만 사용자의 증가와 함께 더욱 다양한 요구사항이 발생하면서 기존 구조로는 한계가 생김.

### 발전된 웹 아키텍처
이를 해결하기 위해 웹 애플리케이션 계층이 추가되면서 아키텍처가 발전:

```
클라이언트 → 웹 서버 → 웹 애플리케이션 → 레포지토리
```

- **웹 서버**: 정적인 파일(HTML, CSS, JS 등)을 서빙
- **웹 애플리케이션**: 동적인 데이터 요청을 처리하고 비즈니스 로직을 수행
- **레포지토리**: 데이터 저장 및 관리

이렇게 분리함으로써 정적 파일과 동적 데이터 요청을 각각의 레이어에서 처리할 수 있게 됨.

### 확장성 문제와 해결 방법
서비스가 성공하면서 클라이언트 요청 수가 점점 증가했지만, 웹 서버와 웹 애플리케이션을 단순히 수평 확장하는 것은 비효율적이었음.

따라서, **관심사의 분리, 관측 가능한 시스템, 효율적인 리소스 사용**을 고려하여 다음과 같은 확장 방법이 도입됨:

1. **게이트웨이 도입**
   - 클라이언트 요청을 게이트웨이가 받아서 적절한 서버로 라우팅
   - API Gateway, Load Balancer 등을 활용하여 트래픽을 분산

2. **여러 개의 서버로 분리**
   - 웹 서버, 웹 애플리케이션을 역할에 따라 세분화하여 확장
   - 마이크로서비스 아키텍처를 도입하여 특정 도메인별로 분리 가능

3. **캐시 도입**
   - 자주 요청되는 데이터를 캐싱하여 데이터베이스 부하를 줄임
   - Redis, Memcached 등을 활용

4. **비동기 처리 도입**
   - 동기적인 요청 처리가 부담이 되는 작업은 메시지 큐(Kafka, RabbitMQ) 등을 활용하여 비동기 처리

이러한 방식으로 시스템을 확장하고, 성능을 최적화하며, 대규모 트래픽을 효과적으로 처리할 수 있도록 아키텍처가 발전함

<br/>
<br/>

## 2. 왜 데이터베이스 병목일까?

### 스케일업 vs 스케일아웃

| 구분 | 스케일업 (Scale-up) | 스케일아웃 (Scale-out) |
|------|--------------------|----------------------|
| 유지보수 관리 | 쉬움 | 여러 노드에 적절한 부하 분산 필요 |
| 확장성 | 제약이 있음 | 상대적으로 자유로움 |
| 장애 복구 | 단일 서버 장애 발생 시 다운타임 있음 | 여러 노드로 장애 탄력성이 있음 |

- **스케일업(Scale-up)**: 하나의 서버의 사양을 높이는 방식 (CPU, RAM 증가)
- **스케일아웃(Scale-out)**: 여러 대의 서버를 추가하여 부하를 분산하는 방식

하지만 **스케일아웃을 적용하더라도 클라이언트 입장에서는 하나의 서버처럼 동작해야 함**. 즉, 같은 입력에 대해 항상 같은 결과를 제공해야 하는데, 이는 데이터베이스의 일관성을 유지하는 것이 핵심 문제로 작용함.

### 데이터베이스 스케일아웃의 어려움
- **데이터베이스는 상태를 관리하는 시스템**
  - 서버는 무상태(Stateless) 아키텍처로 쉽게 확장 가능하지만, 데이터베이스는 데이터를 보존해야 하므로 확장이 어려움.
  - 데이터 일관성을 유지하려면 트랜잭션, 복제, 샤딩 등의 추가적인 비용이 발생.

### 현대 서버 아키텍처의 방향
현대 서버 아키텍처는 **상태 관리를 데이터베이스에 위임**하고, 서버는 상태를 직접 관리하지 않는 방향으로 발전함.

- **서버는 무상태(stateless)화 → 스케일아웃 용이**
- **데이터베이스는 상태 관리 전담 → 확장 비용 증가**

### 데이터베이스 병목의 원인
1. **메모리 vs 디스크 속도 차이**
   - 서버는 메모리에서 데이터를 처리하지만, 데이터베이스는 디스크에서 데이터를 읽어오기 때문에 속도가 느림.

2. **네트워크 속도 영향**
   - 서버와 데이터베이스 간 데이터 전송 시 네트워크 속도에 영향을 받음.
   - 데이터베이스 요청 횟수가 많아질수록 네트워크 지연이 성능 저하의 원인이 됨.

이를 해결하기 위해 캐시, 샤딩, 비동기 데이터 처리 등의 최적화 기법이 도입됨.

<br/>
<br/>

## 3. 대용량 시스템 아키텍처

### 대용량 처리는 왜 어려울까?
1. 하나의 서버 또는 데이터베이스로 감당하기 힘들어 대부분 여러 개의 서버 또는 데이터베이스를 사용함
2. 여러 개의 서버에서 유입되는 데이터의 일관성을 보장할 수 있어야 함
3. 웹 서비스들은 24시간 무중단으로 운영되므로, 잘못된 코드 한 줄이 미치는 영향의 범위가 큼
4. 여러 서비스들이 얽혀 있어 시스템 복잡도가 상당히 높아짐

### 대용량 서비스는 어떤 조건을 만족해야 할까?
1. **고가용성**: 언제든 서비스를 이용할 수 있어야 함
2. **확장성**: 증가하는 데이터와 트래픽에 대응할 수 있어야 함
3. **관측가능성**: 문제가 발생했을 때 신속하게 인지하고 영향을 최소화할 수 있어야 함

### 대용량 시스템의 아키텍처

1. **기본 구조**
- 클라이언트 → 서버 → 데이터베이스
- 문제점: 서버 응답 속도 저하

2. **스케일 아웃 적용 - 트래픽 분산**
- 클라이언트 → 로드밸런서 → 여러 개의 서버 → 데이터베이스
    - 이때 하나의 서버로만 트래픽이 몰리면 의미가 없으니 로드밸런서가 도입 됨
- 문제점: 데이터베이스 부하 증가로 전체 성능 저하

3. **데이터베이스 부하 완화를 위한 캐시 활용**
- 클라이언트 → 로드밸런서 → 여러 개의 서버 → 캐시 | 데이터베이스
  - 로컬 캐시 : 각 서버 메모리에 데이터를 캐싱
    - 글로벌 캐시 : Redis 서버와 같이 여러 서버가 하나의 캐시를 바라볼 수 있게 하는 것
- 캐시의 주기, 만료 정책 고려 필요

4. **외부 시스템의 속도 제약을 고려한 비동기 큐 도입**
- 클라이언트 → 로드밸런서 → 여러 개의 서버 → 비동기 큐 → 외부 기관
  - 대외기관의 TPS(초당 트랜잭션) 제한에 맞춰 서버 요청 조절 가능
> 비동기 큐를 이용함으로써 클라이언트 요청 중 대외기관과 연동되는 트랜잭션을 클라이언트 요청에서 제외 
> 클라이언트를 요청을 보낼 때 서버의 데이터에만 의존하게 되고 , 비동기큐를 이용함으로써 대외기관이 받을 수 있는 적정 tps 에 맞춰 서버의 요청도 조절 가능 

