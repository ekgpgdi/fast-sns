package dahye.fastsns.fastsns.controller;

import dahye.fastsns.fastsns.member.dto.MemberDto;
import dahye.fastsns.fastsns.member.dto.RegisterMemberCommand;
import dahye.fastsns.fastsns.member.service.MemberReadService;
import dahye.fastsns.fastsns.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {
    final private MemberWriteService memberWriteService;
    final private MemberReadService memberReadService;

    @PostMapping("/members")
    public MemberDto register(@RequestBody RegisterMemberCommand command) {
        var member = memberWriteService.register(command);
        return memberReadService.toDto(member);
    }

    @GetMapping("/members/{id}")
    public MemberDto getMember(@PathVariable Long id) {
        return memberReadService.getMember(id);
    }
}
