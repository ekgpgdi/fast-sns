package dahye.fastsns.fastsns.util;

import dahye.fastsns.fastsns.post.entity.Post;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;

import static org.jeasy.random.FieldPredicates.*;

public class PostFixtureFactory {

    static public EasyRandom get(Long memberId, LocalDate firstDate, LocalDate lastDate) {
        var idField = named("id").and(ofType(Long.class)
                .and(inClass(Post.class))); // id 는 랜덤값 생성에서 제외

        var memberIdField = named("memberId").and(ofType(Long.class)
                .and(inClass(Post.class))); // memberId 는 랜덤값 생성에서 제외

        var params = new EasyRandomParameters()
                .excludeField(idField)
                .dateRange(firstDate, lastDate) // firstDate ~ lastDate 사이의 랜덤 데이트
                .randomize(memberIdField, () -> memberId);
        return new EasyRandom(params);
    }
}
