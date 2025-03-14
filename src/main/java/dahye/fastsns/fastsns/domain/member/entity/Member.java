package dahye.fastsns.fastsns.domain.member.entity;

import dahye.fastsns.fastsns.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.util.Objects;

/*
    프로젝트가 커질수록 사이드 이펙트를 측정하기 어려워짐.
    Setter는 객체의 상태를 외부에서 변경할 수 있어 의도하지 않은 사이드 이펙트를 일으킬 수 있음.
    따라서 Setter는 꼭 필요한 경우에만 제공하는 것이 좋음.

    객체 상태 변경은 Setter보다는 동작 단위로 메서드를 제공하는 것이 더 안전하고 명확함.
    예: setBalance() 대신 deposit(), withdraw() 메서드를 사용하여 상태 변경을 관리하도록 함.
*/
@Entity
@Getter
@NoArgsConstructor
@Table(name = "member")
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nickname;

    @Column
    private String email;

    @Column
    private LocalDate birthDay;

    final private static Long NAME_MAX_LENGTH = 10L;

    @Builder
    public Member(Long id, String nickname, String email, LocalDate birthDay) {
        this.id = id;
        this.email = Objects.requireNonNull(email);
        this.birthDay = Objects.requireNonNull(birthDay);

        validateNickname(nickname);
        this.nickname = Objects.requireNonNull(nickname);
    }

    /*
    객체의 상태 변경은 객체 내부에서 처리하도록 선호 -> 외부에서의 변경을 최소화하여 사이드 이펙트를 추적하기 쉬워짐
    객체의 상태를 변경하는 로직은 해당 객체 내의 메서드로 제공하여, 외부에서 직접 변경할 수 없도록 관리하는 것이 바람직함.
    */
    public void changeNickname(String to) {
        Objects.requireNonNull(to);
        validateNickname(to);
        nickname = to;
    }

    private void validateNickname(String nickname) {
        Assert.isTrue(nickname.length() <= NAME_MAX_LENGTH, "최대 길이를 초과했습니다.");
    }
}
