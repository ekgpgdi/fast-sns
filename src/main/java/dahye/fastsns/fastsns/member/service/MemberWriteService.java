package dahye.fastsns.fastsns.member.service;

import dahye.fastsns.fastsns.member.dto.RegisterMemberCommand;
import dahye.fastsns.fastsns.member.entity.Member;
import dahye.fastsns.fastsns.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberWriteService {
    final private MemberRepository memberRepository;

    public Member register(RegisterMemberCommand command) {
        /*
            목표 - 회원 정보(이메일, 닉네임, 생년월일) 를 등록한다.
                - 닉네임은 10자를 넘길 수 없다.
            파라미터 - memberRegisterCommand

            Member member = Member.builder().build();
            memberRepository.save()
         */

        var member = Member.builder()
                .nickname(command.nickname())
                .email(command.email())
                .birthDay(command.birthDay())
                .build();
        return memberRepository.save(member);
    }
}
