package dahye.fastsns.fastsns.application.controller;

import dahye.fastsns.fastsns.post.dto.DailyPostCount;
import dahye.fastsns.fastsns.post.dto.DailyPostCountRequest;
import dahye.fastsns.fastsns.post.dto.PostCommand;
import dahye.fastsns.fastsns.post.service.PostReadService;
import dahye.fastsns.fastsns.post.service.PostWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
