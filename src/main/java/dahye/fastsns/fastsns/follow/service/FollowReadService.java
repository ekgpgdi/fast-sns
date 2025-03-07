package dahye.fastsns.fastsns.follow.service;

import dahye.fastsns.fastsns.follow.dto.FollowDto;
import dahye.fastsns.fastsns.follow.entity.Follow;
import dahye.fastsns.fastsns.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowReadService {
    final private FollowRepository followRepository;

    public List<FollowDto> getFollowings(Long memberId) {
        return followRepository.findAllByFromMemberId(memberId).stream().map(this::toDto).toList();
    }

    public FollowDto toDto(Follow follow) {
        return new FollowDto(follow.getId(), follow.getFromMemberId(), follow.getToMemberId(), follow.getCreatedAt());
    }
}
