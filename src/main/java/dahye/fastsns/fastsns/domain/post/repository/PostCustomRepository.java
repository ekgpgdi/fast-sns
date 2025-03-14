package dahye.fastsns.fastsns.domain.post.repository;

import dahye.fastsns.fastsns.domain.post.dto.DailyPostCount;
import dahye.fastsns.fastsns.domain.post.dto.DailyPostCountRequest;
import dahye.fastsns.fastsns.domain.post.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostCustomRepository {
    List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request);

    Optional<Post> findById(Long postId, boolean requiredLock);

    List<Post> findAllByMemberIdOrderByIdDesc(Long memberId, int size);

    List<Post> findAllByIdLessThanAndMemberIdOrderByIdDesc(Long id, Long memberId, int size);

    List<Post> findAllByMemberIdInOrderByIdDesc(List<Long> memberIds, int size);

    List<Post> findAllByIdLessThanAndMemberIdInOrderByIdDesc(Long id, List<Long> memberIds, int size);
}
