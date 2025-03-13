package dahye.fastsns.fastsns.application.usecase;

import dahye.fastsns.fastsns.domain.follow.dto.FollowDto;
import dahye.fastsns.fastsns.domain.follow.service.FollowReadService;
import dahye.fastsns.fastsns.domain.post.dto.PostDto;
import dahye.fastsns.fastsns.domain.post.entity.Post;
import dahye.fastsns.fastsns.domain.post.entity.Timeline;
import dahye.fastsns.fastsns.domain.post.service.PostReadService;
import dahye.fastsns.fastsns.domain.post.service.TimelineReadService;
import dahye.fastsns.fastsns.util.CursorRequest;
import dahye.fastsns.fastsns.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GetTimelinePostUsecase {
    final private FollowReadService followReadService;
    final private PostReadService postReadService;
    final private TimelineReadService timelineReadService;

    public PageCursor<PostDto> execute(Long memberId, CursorRequest cursorRequest) {
        /*
            1. memberId -> follow 조회
            2. 1번 결과로 게시물 조회
         */
        var followings = followReadService.getFollowings(memberId);
        var followingMemberIds = followings.stream().map(FollowDto::toMemberId).toList();
        return postReadService.getPosts(followingMemberIds, cursorRequest);
    }

    public PageCursor<Post> executeByTimeline(Long memberId, CursorRequest cursorRequest) {
        /*
            1.Timeline 조회
            2. 1번에 해당하는 게시물을 조회한다.
         */
        var pageTimelines = timelineReadService.getTimelines(memberId, cursorRequest);
        var postIds = pageTimelines.body().stream().map(Timeline::getPostId).toList();
        var posts = postReadService.getPosts(postIds);

        return new PageCursor<>(pageTimelines.nextCursorRequest(), posts);
    }
}
