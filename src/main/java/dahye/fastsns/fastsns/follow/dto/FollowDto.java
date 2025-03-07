package dahye.fastsns.fastsns.follow.dto;

import java.time.LocalDateTime;

public record FollowDto(Long id,
                        Long fromMemberId,
                        Long toMemberId,
                        LocalDateTime createdAt) {
}
