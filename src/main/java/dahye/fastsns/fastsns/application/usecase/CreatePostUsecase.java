package dahye.fastsns.fastsns.application.usecase;

import dahye.fastsns.fastsns.domain.follow.dto.FollowDto;
import dahye.fastsns.fastsns.domain.follow.service.FollowReadService;
import dahye.fastsns.fastsns.domain.post.dto.PostCommand;
import dahye.fastsns.fastsns.domain.post.service.PostWriteService;
import dahye.fastsns.fastsns.domain.post.service.TimelineWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CreatePostUsecase {
    final private PostWriteService postWriteService;

    final private FollowReadService followReadService;

    final private TimelineWriteService timelineWriteService;

    public Long execute(PostCommand postCommand) {
        var postId = postWriteService.create(postCommand);

        var followingMemberIds = followReadService.getFollowers(postCommand.memberId())
                .stream()
                .map(FollowDto::fromMemberId)
                .toList();

        timelineWriteService.deliveryToTimeline(postId, followingMemberIds);

        return postId;
    }
}
