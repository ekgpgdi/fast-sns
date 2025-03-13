package dahye.fastsns.fastsns.domain.post.service;

import dahye.fastsns.fastsns.domain.member.dto.MemberDto;
import dahye.fastsns.fastsns.domain.post.entity.Post;
import dahye.fastsns.fastsns.domain.post.entity.PostLike;
import dahye.fastsns.fastsns.domain.post.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostLikeWriteService {
    final private PostLikeRepository postLikeRepository;

    public Long create(Post post, MemberDto memberDto) {
        var postLike = PostLike.builder()
                .memberId(memberDto.id())
                .postId(post.getId())
                .build();

        return postLikeRepository.save(postLike).getId();
    }
}
