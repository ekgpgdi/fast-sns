package dahye.fastsns.fastsns.domain.member;

import dahye.fastsns.fastsns.util.MemberFixtureFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberTest {

    @DisplayName("회원은 닉네임을 변경할 수 있다.")
    @Test
    public void testChangeNickname() {
        var member = MemberFixtureFactory.create();
        var expected = "pnu";

        member.changeNickname(expected);
        Assertions.assertEquals(expected, member.getNickname());
    }

    @DisplayName("회원은 닉네임은 10자를 초과할 수 없다.")
    @Test
    public void testNicknameMaxLength() {
        var member = MemberFixtureFactory.create();
        var overMaxLengthNickname = "pnupnupnupnu";

        Assertions.assertThrows(IllegalArgumentException.class, () -> member.changeNickname(overMaxLengthNickname));
    }

    @DisplayName("회원은 닉네임은 Null 일 수 없다.")
    @Test
    public void testNicknameNull() {
        var member = MemberFixtureFactory.create();

        Assertions.assertThrows(NullPointerException.class, () -> member.changeNickname(null));
    }
}
