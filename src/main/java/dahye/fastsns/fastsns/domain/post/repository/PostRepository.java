package dahye.fastsns.fastsns.domain.post.repository;

import dahye.fastsns.fastsns.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostCustomRepository {

    Page<Post> findAllByMemberId(Long memberId, Pageable pageable);

    List<Post> findAllByIdIn(List<Long> ids);
}
