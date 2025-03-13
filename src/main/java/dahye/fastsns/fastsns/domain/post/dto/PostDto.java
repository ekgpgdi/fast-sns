package dahye.fastsns.fastsns.domain.post.dto;

import java.time.LocalDateTime;

public record PostDto(Long id,
                      String content,
                      LocalDateTime createdAt,
                      Long likeCount) {
}
