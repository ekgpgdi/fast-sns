## 목차
- [요구사항](#요구사항)
- [개발 고려 사항](#개발-고려-사항)
- [데이터베이스](#데이터베이스)
- [대용량 시스템에 대한 이해 ](#대용량-시스템에-대한-이해)

<br/>
<br/>

## 요구사항 
### 회원 정보 관리 
1. 이메일, 닉네임, 생년월일을 입력받아 저장한다.
2. 닉네임은 10자를 초과할 수 없다.
3. 회원은 닉네임을 변경할 수 있다.
   - 회원의 닉네임 변경 이력을 조회 할 수 있어야 한다.

### 게시물
1. 게시물 등록
2. 내가 쓴 글 캘린더 : 작성일자와 일자별 회원의 작성한 게시물 갯수를 반환한다.


<br/>
<br/>

## 개발 고려 사항

<br/>

### 1. Setter
1. **Setter는 필요한 경우에만 사용하라** <br/>
   프로젝트가 커지면 클래스가 점점 복잡해지는데, Setter 메서드가 있으면 클래스 외부에서 객체의 상태를 쉽게 변경할 수 있습니다. <br/>
   이는 의도하지 않은 사이드 이펙트를 발생시킬 수 있습니다.  <br/>
   그래서 Setter 메서드는 꼭 필요한 경우에만 제공하는 것이 좋습니다. <br/> <br/>
2. **Setter보다는 동작 단위로 제공하는 것이 더 낫다**<br/>
   객체의 상태를 변경해야 하는 경우, Setter를 노출하는 것보다는 그 상태를 변경하는 동작을 메서드로 제공하는 것이 좋다는 의미입니다.<br/>
   예를 들어, setBalance()와 같은 Setter 대신, deposit() 또는 withdraw() 같은 메서드를 제공하여 객체가 자기 상태를 스스로 관리하게 하는 것이 더 안전하고, 변경할 때 더 명확하게 의도가 드러납니다. <br/> <br/>

🔥 **Setter 대신 메서드로 상태 변경** : 상태를 변경하는 동작을 메서드로 제공하여, 객체가 내부적으로 상태를 관리하도록 합니다. 예를 들어:
```java
public class Account {
    private double balance;

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
        }
    }

    public double getBalance() {
        return balance;
    }
}
```
   
이렇게 하면 객체의 상태가 외부에서 임의로 변경되지 않고, 비즈니스 로직에 맞는 방식으로 상태를 제어할 수 있습니다.

<br/>

### 2. Object Mother 패턴
[Object Mother Blog LINK](https://martinfowler.com/bliki/ObjectMother.html)

: 객체 생성의 복잡성을 줄이고, 테스트 코드에서의 객체 생성을 간소화하기 위한 디자인 패턴 <br/>
> 테스트에서 자주 사용되는 객체를 미리 정의해 놓고 필요할 때마다 재사용할 수 있습니다. 이렇게 하면 객체 생성을 위한 코드가 간결해지고, 테스트의 가독성이 좋아집니다.

<br/>

**Example**
```java
public class CustomerObjectMother {
    public static Customer aCustomerWithDefaultName() {
        return new Customer("John Doe", "john.doe@example.com");
    }

    public static Customer aCustomerWithCustomName(String name) {
        return new Customer(name, "default@example.com");
    }

    public static Customer aCustomerWithCustomEmail(String email) {
        return new Customer("Default Name", email);
    }
}
```
이 예시에서 CustomerObjectMother 클래스는 다양한 이름과 이메일을 가진 Customer 객체를 생성하는 방법을 제공합니다. 이를 통해 테스트에서는 간단하게 필요한 데이터를 제공받을 수 있습니다.

<br/>

### 3. Easy Random 라이브러리 
[easy-random Git LINK](https://github.com/j-easy/easy-random)

: Java용 라이브러리로, 무작위 객체 생성을 쉽게 할 수 있도록 도와줍니다.  <br/>
주로 단위 테스트에서 다양한 객체를 자동으로 생성할 때 유용하게 사용됩니다. <br/>

<br/>

**Example**
```java
public class Example() {
    public void easyRandom() {
        // 기본 설정으로 객체 생성
        EasyRandom easyRandom = new EasyRandom();
        MyClass myObject = easyRandom.nextObject(MyClass.class);

        // 세부 설정을 통한 객체 생성
        EasyRandomParameters parameters = new EasyRandomParameters()
                .randomize(Long.class, new Randomizer<Long>() {
                    public Long getRandomValue() {
                        return 100L; // 모든 Long 타입 값에 대해 100을 반환
                    }
                });

        EasyRandom easyRandom = new EasyRandom(parameters);
        MyClass myObject = easyRandom.nextObject(MyClass.class);
    }
}
```

<br/>

### 4. 정규화 

중복된 데이터이면 반드시 정규화를 해야하는 걸까?
`정규화도 비용이다. 읽기 비용을 지불하고 쓰기 비용을 줄이는 것`

**정규화시 고려해야 하는 것**
- 얼마나 빠르게 데이터의 최신성을 보장해야 하는가?
- 히스토리성 데이터는 오히려 정규화를 하지 않아야 한다.
- 데이터 변경 주기와 조회 주기는 어떻게 되는가?
  - 변경 주기가 조회 주기보다 훨씬 빈번하다면 정규화를 하는 게 이득
- 객체(테이블) 탐색 깊이가 얼마나 깊은가

**정규화를 하기로 했다면 읽기 시 데이터를 어떻게 가져올 것인가?**
- 테이블 조인을 많이 활용하는데, 이건 사실 고민해볼 문제다.
- 테이블 조인은 서로 다른 테이블의 결합도를 엄청나게 높인다.
- 조회 시에는 성능 좋은 별도 데이터베이스나 캐싱 등 다양한 최적화 기법을 이용할 수 있다.
- 조인을 사용하게 되면, 이런 기법들을 사용하는데 제한이 있거나 더 많은 리소스가 들 수 있다.
- 읽기 쿼리 한번 더 발생하는 것은 그렇게 큰 부담이 아닐 수 있다. 

<br/>

### 5. 결합도

#### 1. 조인은 최대한 미루자
- 서로 다른 도메인을 조인하면 강한 결합이 발생하여 유지보수가 어려워짐
- 필요한 데이터만 가져와서 후처리하는 방식이 더 유연함

#### 2. 서로 다른 도메인에 대한 정보가 필요할 때의 해결 방법
도메인 간 결합도를 낮추면서 필요한 정보를 주고받는 방식

##### 2.1 파사드 패턴 (Facade Pattern)
- 여러 복잡한 도메인 로직을 하나의 진입점으로 제공하는 방식
- 클라이언트는 직접 여러 도메인과 상호작용하지 않고, 파사드를 통해 필요한 정보를 얻음
- **예시**:
    - `UserFacade`에서 `UserService`, `OrderService`를 호출하여 사용자 정보와 주문 정보를 한 번에 제공

##### 2.2 오케스트레이션 (Orchestration)
- 하나의 중앙 컨트롤러가 여러 도메인의 흐름을 조정하는 방식
- 한 서비스에서 다른 서비스의 실행을 명시적으로 호출하고, 응답을 받아서 처리
- 주로 동기적인 흐름에서 사용됨
- **예시**:
    - `OrderService`가 `PaymentService`를 호출하여 결제가 완료되면 주문 상태를 업데이트

##### 2.3 유즈케이스 (Use Case Layer) 활용
- 도메인의 흐름을 제어하는 계층
- 각 도메인의 핵심 비즈니스 로직은 그대로 유지하되, 필요한 데이터를 가져오고 조합하는 역할만 수행
- 유즈케이스 계층에서는 비즈니스 로직을 최소화해야 유지보수가 쉬워짐
- **예시**:
    - `PlaceOrderUseCase`가 `UserService`, `OrderService`, `PaymentService`를 호출하여 주문을 처리하지만, 개별 서비스의 비즈니스 로직에는 개입하지 않음

<br/>
<br/>

## 데이터베이스 

<br/>

### 데이터베이스 성능 

|        | 메모리 (RAM) | 디스크 (HDD/SSD) |
|--------|------------|----------------|
| **속도** | 빠름       | 느림           |
| **영속성** | 전원이 공급되지 않으면 휘발 | 영속성이 있음 |
| **가격**  | 비쌈       | 저렴함         |

> 데이터베이스의 데이터는 결국 디스크에 저장됨  
> 따라서 **데이터베이스 성능의 핵심은 디스크 I/O(입출력) 최소화**에 있음


#### 디스크 I/O 최소화를 위한 전략

1. ✅ **메모리 캐시 활용**
- 디스크 접근을 줄이기 위해 **메모리에 있는 데이터로 최대한 요청을 처리**해야 함. 
- 이를 통해 **캐시 히트율**을 높이고 성능을 개선

2. ✅ **쓰기(Write) 최적화**
- 데이터를 바로 디스크에 쓰지 않고 **먼저 메모리에 저장**하여 랜덤 I/O를 줄임
- 하지만, 메모리에 저장된 데이터가 디스크에 기록되지 못한 채 서버가 다운되면 데이터가 유실될 수 있음

3. ✅ **WAL (Write Ahead Log) 사용**
- 데이터 유실을 방지하기 위해 **WAL(Write Ahead Log)** 를 사용하여 변경 이력을 먼저 로그로 남김
- 만약 서버가 다운되더라도 WAL을 이용해 데이터를 복구할 수 있음

#### 디스크 접근 방식

- **랜덤 I/O (Random I/O)**: 무작위 위치에서 데이터를 읽고 씀 → **성능 저하**  
- **순차 I/O (Sequential I/O)**: 연속된 데이터 블록을 읽고 씀 → **성능 향상**  

> 대부분의 트랜잭션은 무작위로 Write가 발생함  
> 따라서 **이를 지연시켜 랜덤 I/O 횟수를 줄이고, 대신 순차적 I/O를 유도하면 성능이 개선됨**  

#### 💡 결론
✅ **업데이트가 올 때마다 디스크에 데이터를 저장하면 비효율적**  
✅ **메모리에 데이터를 쌓아두었다가 한 번에 디스크로 저장하는 것이 성능적으로 유리**  
✅ **디스크의 랜덤 I/O를 최소화하는 것이 데이터베이스 성능 최적화의 핵심**

<br/>

### 인덱스

인덱스는 **정렬된 자료 구조**로, **탐색 범위를 최소화**하여 조회 성능을 높인다.  
하지만 **삽입/삭제 성능**을 저하시킬 수 있다. 인덱스는 **조회 성능을 높이지만, 삽입/삭제 시 성능 저하**를 발생시킨다.

> **인덱스도 하나의 테이블이다.**

#### 자료구조

- **B+ Tree**  
  - 삽입/삭제 시 항상 균형을 이룬다.
  - **리프 노드에 데이터 존재** → 연속 데이터 접근 시 유리.

- **MySQL**의 인덱스는 **PK 인덱스**와 매칭, **Oracle**은 데이터 주소를 사용한다.

#### 클러스터 인덱스

- **클러스터 인덱스**는 **데이터 위치를 결정**하는 키 값이다.
- **PK는 클러스터 인덱스**다.
- **PK 제외 인덱스**는 **PK를 포함**한다.
- **정렬된 순서**로 저장 → **삽입 시 성능 저하** 발생 가능.

#### 세컨더리 인덱스

- 세컨더리 인덱스만으로는 데이터를 찾을 수 없다.
- **세컨더리 인덱스**는 **PK를 포함**해 **커버링 인덱스**를 가능하게 한다.  
  → 테이블에 접근하지 않고 인덱스만으로 데이터를 조회할 수 있다.

#### 장점

1. **PK 활용 검색이 빠름**, 특히 **범위 검색**에서 성능 우위 → **공간적 캐시 이점**.
2. **세컨더리 인덱스는 PK 포함** → **커버링 인덱스** 활용 가능.

### 실습
```sql
# 데이터 분포도에 따라 같은 인덱스여도 성능이 다를 수 있다.
# 데이터 식별성에 따라 성능이 크게 달라짐

# 약 270만개 데이터 조회 40s 432ms > 인덱스를 주지 않았을 때보다 느려짐
# 범위의 조건을 좁힐 수 없음 (현재 1번 유저의 데이터만 있기에)
# 인덱스 테이블도 보고, 물리 테이블도 봐야 하기에 역효과 발생
create index POST__index_member_id
    on POST (memberId);

SELECT createdDate, memberId, count(id) as count
                    FROM POST use index (POST__index_member_id)
                    WHERE memberId = 1 AND createdDate BETWEEN '1900-01-01' AND '2025-03-01'
                    GROUP BY memberId, createdDate;

# 약 270만개 데이터 조회 2s 489ms
# 식별값이 많기에, 대신 데이터가 없는 memberId 인 경우 엄청 느려짐
create index POST__index_created_date
    on POST (createdDate);

SELECT createdDate, memberId, count(id) as count
                    FROM POST use index (POST__index_created_date)
                    WHERE memberId = 1 AND createdDate BETWEEN '1900-01-01' AND '2025-03-01'
                    GROUP BY memberId, createdDate;

# 약 270만개 데이터 조회 129ms
# 복합 인덱스로 추가
create index POST__index_member_id_created_date
    on POST (memberId, createdDate);

SELECT createdDate, memberId, count(id) as count
                    FROM POST use index (POST__index_member_id_created_date)
                    WHERE memberId = 1 AND createdDate BETWEEN '1900-01-01' AND '2025-03-01'
                    GROUP BY memberId, createdDate;
```

### 인덱스를 다룰 때 주의해야 할 점

#### 1. 인덱스 필드 가공
- 인덱스 필드를 가공하면 인덱스를 사용할 수 없음.
    - 예시: `age`가 인덱스가 있다고 해도, `WHERE age * 10 = 1` 쿼리에서는 인덱스를 사용하지 않음.
    - 예시: `WHERE age = '1'`과 같이 타입이 잘못된 경우에도 인덱스를 사용할 수 없음.

#### 2. 복합 인덱스
- 복합 인덱스는 인덱스에 명시된 순서대로 정렬됨.
    - 예시: `A, B` 복합 인덱스에서 `WHERE B`만 사용할 경우 인덱스를 사용하지 않음.
    - **제일 선두 컬럼이 중요**: 복합 인덱스에서 효율적인 사용을 위해 첫 번째 컬럼이 쿼리 조건에 있어야 함.

#### 3. 하나의 쿼리에는 하나의 인덱스만 사용
- 하나의 쿼리에서 여러 인덱스를 동시에 탐색하지 않음.
    - 예외적으로 `index merge hint`를 사용하면 여러 인덱스를 동시에 사용할 수 있음.
- `WHERE`, `ORDER BY`, `GROUP BY`와 같은 조건을 혼합하여 사용할 때 인덱스를 잘 고려해야 함.

#### 추가 사항
- 의도대로 인덱스가 동작하지 않을 수 있으므로, **`EXPLAIN`**을 통해 확인해야 함.
- 인덱스는 비용이 발생함. **쓰기를 희생하고 조회 성능을 얻는 것**이므로 이를 고려해야 함.
- **인덱스로만 해결할 수 있는 문제인지**를 신중하게 판단해야 함.


<br/>
<br/>

## 대용량 시스템에 대한 이해 

### 1. 웹의 기본 아키텍처

#### 초기 웹 아키텍처
과거의 웹 아키텍처는 다음과 같은 형태로 단순하게 구성되었음:

```
클라이언트 → 웹 서버 → 레포지토리
```

- **클라이언트**: 브라우저 등을 통해 요청을 보냄
- **웹 서버**: 요청을 받아 정적인 파일을 제공
- **레포지토리**: 데이터베이스 등 데이터 저장소

하지만 사용자의 증가와 함께 더욱 다양한 요구사항이 발생하면서 기존 구조로는 한계가 생김.

#### 발전된 웹 아키텍처
이를 해결하기 위해 웹 애플리케이션 계층이 추가되면서 아키텍처가 발전:

```
클라이언트 → 웹 서버 → 웹 애플리케이션 → 레포지토리
```

- **웹 서버**: 정적인 파일(HTML, CSS, JS 등)을 서빙
- **웹 애플리케이션**: 동적인 데이터 요청을 처리하고 비즈니스 로직을 수행
- **레포지토리**: 데이터 저장 및 관리

이렇게 분리함으로써 정적 파일과 동적 데이터 요청을 각각의 레이어에서 처리할 수 있게 됨.

#### 확장성 문제와 해결 방법
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

### 2. 왜 데이터베이스 병목일까?

#### 스케일업 vs 스케일아웃

| 구분 | 스케일업 (Scale-up) | 스케일아웃 (Scale-out) |
|------|--------------------|----------------------|
| 유지보수 관리 | 쉬움 | 여러 노드에 적절한 부하 분산 필요 |
| 확장성 | 제약이 있음 | 상대적으로 자유로움 |
| 장애 복구 | 단일 서버 장애 발생 시 다운타임 있음 | 여러 노드로 장애 탄력성이 있음 |

- **스케일업(Scale-up)**: 하나의 서버의 사양을 높이는 방식 (CPU, RAM 증가)
- **스케일아웃(Scale-out)**: 여러 대의 서버를 추가하여 부하를 분산하는 방식

하지만 **스케일아웃을 적용하더라도 클라이언트 입장에서는 하나의 서버처럼 동작해야 함**. 즉, 같은 입력에 대해 항상 같은 결과를 제공해야 하는데, 이는 데이터베이스의 일관성을 유지하는 것이 핵심 문제로 작용함.

#### 데이터베이스 스케일아웃의 어려움
- **데이터베이스는 상태를 관리하는 시스템**
  - 서버는 무상태(Stateless) 아키텍처로 쉽게 확장 가능하지만, 데이터베이스는 데이터를 보존해야 하므로 확장이 어려움.
  - 데이터 일관성을 유지하려면 트랜잭션, 복제, 샤딩 등의 추가적인 비용이 발생.

#### 현대 서버 아키텍처의 방향
현대 서버 아키텍처는 **상태 관리를 데이터베이스에 위임**하고, 서버는 상태를 직접 관리하지 않는 방향으로 발전함.

- **서버는 무상태(stateless)화 → 스케일아웃 용이**
- **데이터베이스는 상태 관리 전담 → 확장 비용 증가**

#### 데이터베이스 병목의 원인
1. **메모리 vs 디스크 속도 차이**
   - 서버는 메모리에서 데이터를 처리하지만, 데이터베이스는 디스크에서 데이터를 읽어오기 때문에 속도가 느림.

2. **네트워크 속도 영향**
   - 서버와 데이터베이스 간 데이터 전송 시 네트워크 속도에 영향을 받음.
   - 데이터베이스 요청 횟수가 많아질수록 네트워크 지연이 성능 저하의 원인이 됨.

이를 해결하기 위해 캐시, 샤딩, 비동기 데이터 처리 등의 최적화 기법이 도입됨.

<br/>
<br/>

### 3. 대용량 시스템 아키텍처

#### 대용량 처리는 왜 어려울까?
1. 하나의 서버 또는 데이터베이스로 감당하기 힘들어 대부분 여러 개의 서버 또는 데이터베이스를 사용함
2. 여러 개의 서버에서 유입되는 데이터의 일관성을 보장할 수 있어야 함
3. 웹 서비스들은 24시간 무중단으로 운영되므로, 잘못된 코드 한 줄이 미치는 영향의 범위가 큼
4. 여러 서비스들이 얽혀 있어 시스템 복잡도가 상당히 높아짐

#### 대용량 서비스는 어떤 조건을 만족해야 할까?
1. **고가용성**: 언제든 서비스를 이용할 수 있어야 함
2. **확장성**: 증가하는 데이터와 트래픽에 대응할 수 있어야 함
3. **관측가능성**: 문제가 발생했을 때 신속하게 인지하고 영향을 최소화할 수 있어야 함

#### 대용량 시스템의 아키텍처

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

