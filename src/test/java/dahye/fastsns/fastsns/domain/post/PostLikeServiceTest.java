package dahye.fastsns.fastsns.domain.post;

import dahye.fastsns.fastsns.domain.member.dto.MemberDto;
import dahye.fastsns.fastsns.domain.post.entity.Post;
import dahye.fastsns.fastsns.domain.post.repository.PostLikeRepository;
import dahye.fastsns.fastsns.domain.post.service.PostLikeWriteService;
import dahye.fastsns.fastsns.util.MemberFixtureFactory;
import dahye.fastsns.fastsns.util.PostFixtureFactory;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;


@SpringBootTest
public class PostLikeServiceTest {

    @Autowired
    private PostLikeWriteService postLikeWriteService;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private Post post;

    private final List<MemberDto> members = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        for (int i = 0; i < 10; i++) {
            var member = MemberFixtureFactory.create((long) i);
            members.add(new MemberDto((long) i, member.getEmail(), member.getNickname(), member.getBirthDay()));
        }

        post = PostFixtureFactory.create(0L);
    }

    @Test
    @Transactional
    @DisplayName("10명이 좋아요를 추가하고 5명이 좋아요를 삭제하는 동시성 테스트")
    void testConcurrentLikeAndRemove() throws InterruptedException {
        // 동시성 테스트를 위한 ExecutorService
        ExecutorService executorService = Executors.newFixedThreadPool(15);

        for (var member : members) {
            executorService.submit(() -> {
                try {
                    postLikeWriteService.create(post, member);  // 좋아요 추가
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }


        for (int i = 0; i < 5; i++) {
            var member = members.get(i);
            executorService.submit(() -> {
                try {
                    postLikeWriteService.updateRedis(post.getId(), -1);  // 좋아요 삭제
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        Assertions.assertEquals("5", redisTemplate.opsForValue().get("post_like:" + post.getId()), "Redis 에는 5개의 좋아요가 남아있어야 합니다.");
    }

    @AfterEach
    public void clear() {
        redisTemplate.delete("post_like:" + post.getId());
    }
}

