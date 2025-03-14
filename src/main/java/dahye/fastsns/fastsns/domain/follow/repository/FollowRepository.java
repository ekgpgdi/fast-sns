package dahye.fastsns.fastsns.domain.follow.repository;

import dahye.fastsns.fastsns.domain.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByFromMemberId(Long fromMemberId);

    List<Follow> findAllByToMemberId(Long toMemberId);
}
