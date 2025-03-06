package dahye.fastsns.fastsns.member.dto;

import java.time.LocalDate;

/**
 * record 로 선언하면, 컴파일러가 자동으로 getter, setter, equals(), hashCode(), toString() 등을 생성해줌
 */
public record RegisterMemberCommand (
    String email,
    String nickname,
    LocalDate birthDay
) {

}
