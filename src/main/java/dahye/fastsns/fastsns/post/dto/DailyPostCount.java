package dahye.fastsns.fastsns.post.dto;

import java.time.LocalDate;

public record DailyPostCount(Long memberId,
                             LocalDate date,
                             Long postCount) {
}
