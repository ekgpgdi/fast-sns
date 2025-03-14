package dahye.fastsns.fastsns.domain.member.repository;

import dahye.fastsns.fastsns.domain.member.entity.MemberNicknameHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface MemberNicknameHistoryRepository extends JpaRepository<MemberNicknameHistory, Long> {
    List<MemberNicknameHistory> findAllByMemberId(Long memberId);
}
