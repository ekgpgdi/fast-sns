package dahye.fastsns.fastsns.domain.post;

import dahye.fastsns.fastsns.domain.post.entity.Post;
import dahye.fastsns.fastsns.domain.post.repository.PostJdbcRepository;
import dahye.fastsns.fastsns.util.PostFixtureFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.util.stream.IntStream;

@SpringBootTest
public class PostBulkInsertTest {
    @Autowired
    private PostJdbcRepository postJdbcRepository;

    @Test
    public void bulkInsert() {
        var easyRandom = PostFixtureFactory.get(
                1L,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 2, 1)
        );

        var stopWatch = new StopWatch();
        stopWatch.start();

        var posts = IntStream.range(0, 10000 * 100)
                .parallel() // 병렬 처리
                .mapToObj(i -> easyRandom.nextObject(Post.class))
                .toList();

        stopWatch.stop();
        System.out.println("객체 생성 시간 : " + stopWatch.getTotalTimeSeconds());
        // 100만건 객체 생성 시간 : 11.449157975

        var queryStopWatch = new StopWatch();
        queryStopWatch.start();

        postJdbcRepository.bulkInsert(posts);

        queryStopWatch.stop();
        System.out.println("DB INSERT 시간 : " + queryStopWatch.getTotalTimeSeconds());
        // 100만건 DB INSERT 시간 : 29.727764223
        // Mysql CPU 보는 법 : lsof -i:3306 로 pid 찾고 top -pid {pid} 로 확인
    }
}
