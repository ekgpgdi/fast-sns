package dahye.fastsns.fastsns.application.controller;

import dahye.fastsns.fastsns.application.usecase.CreateFollowMemberUsecase;
import dahye.fastsns.fastsns.application.usecase.GetFollowingMembersUsecase;
import dahye.fastsns.fastsns.domain.member.dto.MemberDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class FollowController {
    final private CreateFollowMemberUsecase createFollowMemberUsecase;
    final private GetFollowingMembersUsecase getFollowingMembersUsecase;

    @PostMapping("/{fromId}/{toId}")
    public void register(@Parameter(name = "fromId") @PathVariable Long fromId,
                         @Parameter(name = "toId") @PathVariable Long toId) {
        createFollowMemberUsecase.execute(fromId, toId);
    }

    @GetMapping("/members/{fromId}")
    public List<MemberDto> getFollowers(@PathVariable Long fromId) {
        return getFollowingMembersUsecase.execute(fromId);
    }
}
