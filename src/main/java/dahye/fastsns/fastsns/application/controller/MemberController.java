package dahye.fastsns.fastsns.application.controller;

import dahye.fastsns.fastsns.domain.member.dto.MemberDto;
import dahye.fastsns.fastsns.domain.member.dto.MemberNicknameHistoryDto;
import dahye.fastsns.fastsns.domain.member.dto.RegisterMemberCommand;
import dahye.fastsns.fastsns.domain.member.service.MemberReadService;
import dahye.fastsns.fastsns.domain.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {
    final private MemberWriteService memberWriteService;
    final private MemberReadService memberReadService;

    @PostMapping
    public MemberDto register(@RequestBody RegisterMemberCommand command) {
        var member = memberWriteService.register(command);
        return memberReadService.toDto(member);
    }

    @GetMapping("/{id}")
    public MemberDto getMember(@PathVariable Long id) {
        return memberReadService.getMember(id);
    }

    @PutMapping("/{id}/name")
    public MemberDto changeNickname(@PathVariable Long id,
                                    @RequestBody String nickname) {
        memberWriteService.changeNickname(id, nickname);
        return memberReadService.getMember(id);
    }

    @GetMapping("/{memberId}/nickname-histories")
    public List<MemberNicknameHistoryDto> getNicknameHistories(@PathVariable Long memberId) {
        return memberReadService.getNicknameHistories(memberId);
    }
}
