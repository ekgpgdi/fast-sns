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
    
