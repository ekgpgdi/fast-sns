package dahye.fastsns.fastsns.domain.post.repository;

import dahye.fastsns.fastsns.domain.post.entity.Timeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimelineRepository extends JpaRepository<Timeline, Long>, TimelineCustomRepository {
}
