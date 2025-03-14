package dahye.fastsns.fastsns.domain.member.repository;

import dahye.fastsns.fastsns.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long id);

    List<Member> findAllByIdIn(List<Long> ids);
}
