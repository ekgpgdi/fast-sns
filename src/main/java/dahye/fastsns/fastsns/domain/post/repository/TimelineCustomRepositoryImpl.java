package dahye.fastsns.fastsns.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dahye.fastsns.fastsns.domain.post.entity.Timeline;
import jakarta.persistence.EntityManager;

import java.util.List;

import static dahye.fastsns.fastsns.domain.post.entity.QTimeline.timeline;

public class TimelineCustomRepositoryImpl implements TimelineCustomRepository {
    private JPAQueryFactory jpaQueryFactory;

    public TimelineCustomRepositoryImpl(EntityManager em) {
        jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Timeline> findAllByIdLessThanAndMemberIdOrderIdByIdDesc(Long id, Long memberId, int size) {
        return jpaQueryFactory.selectFrom(timeline)
                .where(timeline.memberId.eq(memberId)
                        .and(timeline.id.lt(id)))
                .limit(size).fetch();
    }

    @Override
    public List<Timeline> findAllByMemberIdOrderIdByIdDesc(Long memberId, int size) {
        return jpaQueryFactory.selectFrom(timeline)
                .where(timeline.memberId.eq(memberId))
                .limit(size).fetch();
    }
}
