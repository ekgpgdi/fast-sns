package dahye.fastsns.fastsns.application.usecase;

import dahye.fastsns.fastsns.domain.follow.dto.FollowDto;
import dahye.fastsns.fastsns.domain.follow.service.FollowReadService;
import dahye.fastsns.fastsns.domain.member.dto.MemberDto;
import dahye.fastsns.fastsns.domain.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetFollowingMembersUsecase {
    private final FollowReadService followReadService;
    private final MemberReadService memberReadService;

    public List<MemberDto> execute(Long memberId) {
        /*
            1. fromMemberId = memberID -> Follow List
            2. 1번을 순회하면서 회원 정보를 조회
         */

        var followList = followReadService.getFollowings(memberId);
        var followingMemberIds = followList.stream().map(FollowDto::toMemberId).toList();
        return memberReadService.getMembers(followingMemberIds);
    }
}
