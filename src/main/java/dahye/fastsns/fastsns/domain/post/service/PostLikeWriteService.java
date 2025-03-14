package dahye.fastsns.fastsns.domain.post.service;

import dahye.fastsns.fastsns.domain.member.dto.MemberDto;
import dahye.fastsns.fastsns.domain.post.entity.Post;
import dahye.fastsns.fastsns.domain.post.entity.PostLike;
import dahye.fastsns.fastsns.domain.post.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostLikeWriteService {
    final private PostLikeRepository postLikeRepository;
    final private StringRedisTemplate redisTemplate;

    public void updateRedis(Long postId, int count) {
        String REDIS_POST_LIKE_KEY = "post_like:";
        String redisKey = REDIS_POST_LIKE_KEY + postId;

        if (count < 0) redisTemplate.opsForValue().decrement(redisKey, count);
        else redisTemplate.opsForValue().increment(redisKey, count);
    }

    public Long create(Post post, MemberDto memberDto) {
        if (postLikeRepository.existsByPostIdAndMemberId(post.getId(), memberDto.id())) {
            throw new IllegalArgumentException("Already liked this post");
        }

        var postLike = PostLike.builder()
                .memberId(memberDto.id())
                .postId(post.getId())
                .build();

        updateRedis(post.getId(), +1);
        return postLikeRepository.save(postLike).getId();
    }

    public void delete(Post post, MemberDto memberDto) {
        PostLike postLike = postLikeRepository.findByPostIdAndMemberId(post.getId(), memberDto.id())
                .orElseThrow(() -> new IllegalArgumentException("Like not found"));
        postLikeRepository.delete(postLike);
        updateRedis(post.getId(), -1);
    }
}
