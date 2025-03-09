package dahye.fastsns.fastsns.application.controller;

import dahye.fastsns.fastsns.post.dto.DailyPostCount;
import dahye.fastsns.fastsns.post.dto.DailyPostCountRequest;
import dahye.fastsns.fastsns.post.dto.PostCommand;
import dahye.fastsns.fastsns.post.entity.Post;
import dahye.fastsns.fastsns.post.service.PostReadService;
import dahye.fastsns.fastsns.post.service.PostWriteService;
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

    @PostMapping("")
    public Long create(PostCommand command) {
        return postWriteService.create(command);
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
}
