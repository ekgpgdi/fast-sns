package dahye.fastsns.fastsns.domain.post.repository;

import dahye.fastsns.fastsns.domain.post.entity.Timeline;

import java.util.List;

public interface TimelineCustomRepository {
    List<Timeline> findAllByIdLessThanAndMemberIdOrderIdByIdDesc(Long id, Long memberId, int size);

    List<Timeline> findAllByMemberIdOrderIdByIdDesc(Long memberId, int size);
}
