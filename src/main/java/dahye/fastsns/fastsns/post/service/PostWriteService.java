package dahye.fastsns.fastsns.post.service;

import dahye.fastsns.fastsns.post.dto.PostCommand;
import dahye.fastsns.fastsns.post.entity.Post;
import dahye.fastsns.fastsns.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PostWriteService {
    final private PostRepository postRepository;

    public Long create(PostCommand command) {
        var post = Post.builder()
                .memberId(command.memberId())
                .contents(command.contents())
                .build();

        return postRepository.save(post).getId();
    }
}
