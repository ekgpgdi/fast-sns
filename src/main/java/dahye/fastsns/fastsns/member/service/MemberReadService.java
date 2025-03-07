package dahye.fastsns.fastsns.member.service;

import dahye.fastsns.fastsns.member.dto.MemberDto;
import dahye.fastsns.fastsns.member.entity.Member;
import dahye.fastsns.fastsns.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberReadService {
    final private MemberRepository memberRepository;

    public MemberDto getMember(Long id) {
        var member = memberRepository.findById(id).orElseThrow();
        return toDto(member);
    }

    public MemberDto toDto(Member member) {
        return new MemberDto(member.getId(), member.getEmail(), member.getNickname(), member.getBirthDay());
    }
}
