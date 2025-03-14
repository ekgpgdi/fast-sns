package dahye.fastsns.fastsns.domain.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import dahye.fastsns.fastsns.domain.post.dto.DailyPostCount;
import dahye.fastsns.fastsns.domain.post.dto.DailyPostCountRequest;
import dahye.fastsns.fastsns.domain.post.entity.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;

import static com.querydsl.core.types.ExpressionUtils.count;
import static dahye.fastsns.fastsns.domain.post.entity.QPost.post;

public class PostCustomRepositoryImpl implements PostCustomRepository {
    private JPAQueryFactory jpaQueryFactory;

    public PostCustomRepositoryImpl(EntityManager em) {
        jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request) {

        return jpaQueryFactory.select(Projections.constructor(DailyPostCount.class,
                        post.memberId,
                        post.createdDate,
                        count(post.id)))
                .from(post)
                .where(post.memberId.eq(request.memberId())
                        .and(post.createdDate.between(request.firstDate(), request.lastDate())))
                .groupBy(post.createdDate)
                .fetch();
    }

    @Override
    public Optional<Post> findById(Long postId, boolean requiredLock) {
        JPAQuery<Post> query = jpaQueryFactory.selectFrom(post)
                .where(post.id.eq(postId));

        if (requiredLock) {
            query.setLockMode(LockModeType.PESSIMISTIC_WRITE);
        }

        Post result = query.fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public List<Post> findAllByMemberIdOrderByIdDesc(Long memberId, int size) {
        return jpaQueryFactory.selectFrom(post)
                .where(post.memberId.eq(memberId))
                .limit(size).fetch();
    }

    @Override
    public List<Post> findAllByIdLessThanAndMemberIdOrderByIdDesc(Long id, Long memberId, int size) {
        return jpaQueryFactory.selectFrom(post)
                .where(post.memberId.eq(memberId)
                        .and(post.id.lt(id)))
                .limit(size).fetch();
    }

    @Override
    public List<Post> findAllByMemberIdInOrderByIdDesc(List<Long> memberIds, int size) {
        return jpaQueryFactory.selectFrom(post)
                .where(post.memberId.in(memberIds))
                .limit(size).fetch();
    }

    @Override
    public List<Post> findAllByIdLessThanAndMemberIdInOrderByIdDesc(Long id, List<Long> memberIds, int size) {
        return jpaQueryFactory.selectFrom(post)
                .where(post.memberId.in(memberIds)
                        .and(post.id.lt(id)))
                .limit(size).fetch();
    }
}
