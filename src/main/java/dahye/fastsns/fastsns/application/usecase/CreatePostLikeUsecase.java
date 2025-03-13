package dahye.fastsns.fastsns.application.usecase;

import dahye.fastsns.fastsns.domain.member.service.MemberReadService;
import dahye.fastsns.fastsns.domain.post.service.PostLikeWriteService;
import dahye.fastsns.fastsns.domain.post.service.PostReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreatePostLikeUsecase {
    final private MemberReadService memberReadService;
    final private PostReadService postReadService;
    final private PostLikeWriteService postLikeService;

    public void execute(Long postId, Long memberId) {
        var post = postReadService.getPost(postId);
        var member = memberReadService.getMember(memberId);
        postLikeService.create(post, member);
    }
}
