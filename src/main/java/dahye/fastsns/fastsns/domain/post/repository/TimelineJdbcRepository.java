package dahye.fastsns.fastsns.domain.post.repository;

import dahye.fastsns.fastsns.domain.post.entity.Timeline;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class TimelineJdbcRepository {
    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    static final private String TABLE = "timeline";

    public void bulkInsert(List<Timeline> timelines) {
        var sql = String.format("INSERT INTO `%s` (member_id, post_id, created_at) " +
                "VALUES(:memberId, :postId, :createdAt) ", TABLE);

        SqlParameterSource[] params = timelines.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

}
