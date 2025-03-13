package dahye.fastsns.fastsns.application.controller;

import dahye.fastsns.fastsns.application.usecase.CreatePostUsecase;
import dahye.fastsns.fastsns.application.usecase.GetTimelinePostUsecase;
import dahye.fastsns.fastsns.domain.post.dto.DailyPostCount;
import dahye.fastsns.fastsns.domain.post.dto.DailyPostCountRequest;
import dahye.fastsns.fastsns.domain.post.dto.PostCommand;
import dahye.fastsns.fastsns.domain.post.entity.Post;
import dahye.fastsns.fastsns.domain.post.service.PostReadService;
import dahye.fastsns.fastsns.domain.post.service.PostWriteService;
import dahye.fastsns.fastsns.util.CursorRequest;
import dahye.fastsns.fastsns.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/posts")
public class PostController {
    final private PostWriteService postWriteService;
    final private PostReadService postReadService;
    final private GetTimelinePostUsecase getTimelinePostUsecase;
    final private CreatePostUsecase createPostUsecase;

    @PostMapping("")
    public Long create(PostCommand command) {
        return createPostUsecase.execute(command);
    }

    @GetMapping("/daily-post-counts")
    public List<DailyPostCount> getDailyPostCounts(DailyPostCountRequest request) {
        return postReadService.getDailyPostCount(request);
    }

    @GetMapping("/members/{memberId}")
    public Page<Post> getPosts(@PathVariable Long memberId,
                               @RequestParam Integer page,
                               @RequestParam Integer size) {
        return postReadService.getPosts(memberId, PageRequest.of(page, size));
    }

    @GetMapping("/members/{memberId}/by-cursor")
    public PageCursor<Post> getPosts(@PathVariable Long memberId,
                                     CursorRequest request) {
        return postReadService.getPosts(memberId, request);
    }

//    @GetMapping("/members/{memberId}/timeline")
//    public PageCursor<Post> getTimeline(@PathVariable Long memberId,
//                                        CursorRequest request) {
//        return getTimelinePostUsecase.execute(memberId, request);
//    }

    @GetMapping("/members/{memberId}/timeline")
    public PageCursor<Post> getTimeline(@PathVariable Long memberId,
                                        CursorRequest request) {
        return getTimelinePostUsecase.executeByTimeline(memberId, request);
    }

    @PostMapping("/{postId}/like")
    public void likePost(@PathVariable Long postId) {
        postWriteService.likePost(postId);
    }
}
