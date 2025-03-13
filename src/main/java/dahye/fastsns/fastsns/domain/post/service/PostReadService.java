package dahye.fastsns.fastsns.domain.post.service;

import dahye.fastsns.fastsns.domain.post.dto.DailyPostCount;
import dahye.fastsns.fastsns.domain.post.dto.DailyPostCountRequest;
import dahye.fastsns.fastsns.domain.post.dto.PostDto;
import dahye.fastsns.fastsns.domain.post.entity.Post;
import dahye.fastsns.fastsns.domain.post.repository.PostLikeRepository;
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
    final private PostLikeRepository postLikeRepository;

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

    public PostDto toDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getContents(),
                post.getCreatedAt(),
                postLikeRepository.getCount(post.getId())
        );
    }

    public Page<PostDto> getPosts(Long memberId, PageRequest pageRequest) {
        return postRepository.findAllByMemberId(memberId, pageRequest).map(this::toDto);
    }

    public PageCursor<PostDto> getPosts(Long memberId, CursorRequest cursorRequest) {
        /*
        키가 있을 때와 없을 때에 따라서 분기
        1. select * from POST where memberId = 4
        2. select * from POST where memberId = 4 and id < 2;
         */
        var posts = findAllBy(memberId, cursorRequest)
                .stream().map(this::toDto).toList();
        var nextKey = getNextKey(posts);
        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    public Post getPost(Long postId) {
        return postRepository.findById(postId, false).orElseThrow();
    }

    public List<Post> findAllBy(Long memberId, CursorRequest cursorRequest) {

        if (cursorRequest.hasKey()) {
            return postRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
        }

        return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());
    }

    public PageCursor<PostDto> getPosts(List<Long> memberIds, CursorRequest cursorRequest) {
        /*
        키가 있을 때와 없을 때에 따라서 분기
        1. select * from POST where memberId = 4
        2. select * from POST where memberId = 4 and id < 2;
         */
        var posts = findAllBy(memberIds, cursorRequest)
                .stream().map(this::toDto).toList();
        var nextKey = getNextKey(posts);
        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    public List<Post> getPosts(List<Long> ids) {
        return postRepository.findAllByInId(ids);
    }

    public List<Post> findAllBy(List<Long> memberIds, CursorRequest cursorRequest) {

        if (cursorRequest.hasKey()) {
            return postRepository.findAllByLessThanIdAndInMemberIdAndOrderByIdDesc(cursorRequest.key(), memberIds, cursorRequest.size());
        }

        return postRepository.findAllByInMemberIdAndOrderByIdDesc(memberIds, cursorRequest.size());
    }

    private static long getNextKey(List<PostDto> posts) {
        return posts.stream().mapToLong(PostDto::id).min().orElse(CursorRequest.NON_KEY);
    }
}
