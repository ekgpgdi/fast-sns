package dahye.fastsns.fastsns.member.service;

import dahye.fastsns.fastsns.member.dto.RegisterMemberCommand;
import dahye.fastsns.fastsns.member.entity.Member;
import dahye.fastsns.fastsns.member.entity.MemberNicknameHistory;
import dahye.fastsns.fastsns.member.repository.MemberNicknameHistoryRepository;
import dahye.fastsns.fastsns.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberWriteService {
    final private MemberRepository memberRepository;
    final private MemberNicknameHistoryRepository memberNicknameHistoryRepository;

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

        var saveMember = memberRepository.save(member);
        saveNicknameHistory(saveMember);
        return saveMember;
    }

    public void changeNickname(Long memberId, String nickname) {
        /*
            1. 회원의 이름을 변경
            2. 변경 내역을 저장한다.
         */

        var member = memberRepository.findById(memberId).orElseThrow();
        member.changeNickname(nickname);
        memberRepository.save(member);
        saveNicknameHistory(member);
    }

    private void saveNicknameHistory(Member member) {
        var memberNicknameHistory = MemberNicknameHistory.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
        memberNicknameHistoryRepository.save(memberNicknameHistory);
    }
}
