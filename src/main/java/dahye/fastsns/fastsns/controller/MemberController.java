package dahye.fastsns.fastsns.controller;

import dahye.fastsns.fastsns.member.dto.RegisterMemberCommand;
import dahye.fastsns.fastsns.member.entity.Member;
import dahye.fastsns.fastsns.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {
    final private MemberWriteService memberWriteService;

    @PostMapping("/members")
    public Member register(@RequestBody RegisterMemberCommand command) {
        return memberWriteService.create(command);
    }
}
