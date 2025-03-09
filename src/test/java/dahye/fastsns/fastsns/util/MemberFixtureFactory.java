package dahye.fastsns.fastsns.util;

import dahye.fastsns.fastsns.domain.member.entity.Member;
import org.jeasy.random.*;

public class MemberFixtureFactory {
    static public Member create() {
        var param = new EasyRandomParameters();
        return new EasyRandom(param).nextObject(Member.class);
    }

    static public Member create(Long seed) {
        var param = new EasyRandomParameters().seed(seed); // easyRandom 은 seed 가 같으면 같은 값을 반환
        return new EasyRandom(param).nextObject(Member.class);
    }
}
