package dahye.fastsns.fastsns.domain.post.service;

import dahye.fastsns.fastsns.domain.post.entity.Timeline;
import dahye.fastsns.fastsns.domain.post.repository.TimelineRepository;
import dahye.fastsns.fastsns.util.CursorRequest;
import dahye.fastsns.fastsns.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TimelineReadService {
    final private TimelineRepository timelineRepository;

    public PageCursor<Timeline> getTimelines(Long memberId, CursorRequest cursorRequest) {
        var timelines = findAllBy(memberId, cursorRequest);
        var nextKey = timelines.stream().mapToLong(Timeline::getId).min().orElse(CursorRequest.NON_KEY);

        return new PageCursor<>(cursorRequest.next(nextKey), timelines);
    }

    private List<Timeline> findAllBy(Long memberId, CursorRequest cursorRequest) {
        if (cursorRequest.hasKey()) {
            return timelineRepository.findAllByIdLessThanAndMemberIdOrderIdByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
        }

        return timelineRepository.findAllByMemberIdOrderIdByIdDesc(memberId, cursorRequest.size());
    }
}
