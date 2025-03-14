package dahye.fastsns.fastsns.util;

import dahye.fastsns.fastsns.domain.member.entity.Member;
import org.jeasy.random.*;
import org.jeasy.random.randomizers.text.StringRandomizer;

import static org.jeasy.random.FieldPredicates.*;

public class MemberFixtureFactory {

    static public Member create() {
        var param = new EasyRandomParameters();
        return new EasyRandom(param).nextObject(Member.class);
    }

    static public Member create(Long seed) {
        var idField = named("id").and(ofType(Long.class).and(inClass(Member.class))); // id 필드 제외

        var param = new EasyRandomParameters()
                .seed(seed)
                .excludeField(idField) // id 필드를 제외
                .randomize(String.class, new StringRandomizer(10));
        return new EasyRandom(param).nextObject(Member.class);
    }
}
