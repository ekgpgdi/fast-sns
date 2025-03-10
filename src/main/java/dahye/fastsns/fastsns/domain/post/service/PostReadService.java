package dahye.fastsns.fastsns.domain.post.service;

import dahye.fastsns.fastsns.domain.post.dto.DailyPostCount;
import dahye.fastsns.fastsns.domain.post.dto.DailyPostCountRequest;
import dahye.fastsns.fastsns.domain.post.entity.Post;
import dahye.fastsns.fastsns.domain.post.repository.PostRepository;
import dahye.fastsns.fastsns.util.CursorRequest;
import dahye.fastsns.fastsns.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostReadService {
    final private PostRepository postRepository;

    public List<DailyPostCount> getDailyPostCount(DailyPostCountRequest dailyPostCountRequest) {
        /*
            반환 값 : 리스트 [작성일자, 작성회원, 작성 게시물 갯수]

            1.select *
            from Post
            where memberId = :memberId and createdDate between :firstDate and :lastDate
            group by createdDate memberId
         */

        return postRepository.groupByCreatedDate(dailyPostCountRequest);
    }

    public Page<Post> getPosts(Long memberId, PageRequest pageRequest) {
        return postRepository.findAllByMemberId(memberId, pageRequest);
    }

    public PageCursor<Post> getPosts(Long memberId, CursorRequest cursorRequest) {
        /*
        키가 있을 때와 없을 때에 따라서 분기
        1. select * from POST where memberId = 4
        2. select * from POST where memberId = 4 and id < 2;
         */
        var posts = findAllBy(memberId, cursorRequest);
        var nextKey = posts.stream().mapToLong(Post::getId).min().orElse(CursorRequest.NON_KEY);
        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    public List<Post> findAllBy(Long memberId, CursorRequest cursorRequest) {

        if (cursorRequest.hasKey()) {
            return postRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
        }

        return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
    }
}
