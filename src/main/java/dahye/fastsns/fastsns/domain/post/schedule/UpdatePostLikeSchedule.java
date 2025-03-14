package dahye.fastsns.fastsns.domain.post.schedule;

import dahye.fastsns.fastsns.domain.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class UpdatePostLikeSchedule {
    final private PostRepository postRepository;
    private final StringRedisTemplate redisTemplate;

    @Scheduled(fixedRate = 60000)
    public void updatePostLikes() {
        Set<String> keys = redisTemplate.keys("post_like:*");

        for (String key : keys) {
            var postId = Long.parseLong(key.split(":")[1]);
            var likeCountStr = redisTemplate.opsForValue().get(key);
            var likeCount = Long.parseLong(likeCountStr);

            var post = postRepository.findById(postId)
                    .orElseThrow(() -> new EntityNotFoundException("Post not found"));

            post.updateLikeCount(likeCount);
            postRepository.save(post);
        }
    }
}
