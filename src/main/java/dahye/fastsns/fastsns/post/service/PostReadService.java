package dahye.fastsns.fastsns.post.service;

import dahye.fastsns.fastsns.post.dto.DailyPostCount;
import dahye.fastsns.fastsns.post.dto.DailyPostCountRequest;
import dahye.fastsns.fastsns.post.entity.Post;
import dahye.fastsns.fastsns.post.repository.PostRepository;
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
}
