package dahye.fastsns.fastsns.domain.post.repository;

import dahye.fastsns.fastsns.domain.post.entity.PostLike;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Repository
public class PostLikeRepository {
    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    static final private String TABLE = "PostLike";

    static final RowMapper<PostLike> rowMapper = (ResultSet resultSet, int rowNum)
            -> PostLike.builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .postId(resultSet.getLong("postId"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    public Long getCount(Long postId) {
        var sql = String.format("SELECT COUNT(*) FROM %s WHERE postId = :postId ", TABLE);
        var param = new MapSqlParameterSource().addValue("postId", postId);
        return namedParameterJdbcTemplate.queryForObject(sql, param, Long.class);
    }

    public PostLike save(PostLike postLike) {
        /*
        member id 를 보고 갱신 또는 삽입을 정함
        반환값은 id 를 담아서 반환한다.
         */
        if (postLike.getId() == null) {
            return insert(postLike);
        }
        throw new UnsupportedOperationException("PostLike 는 갱신을 지원하지 않습니다.");
    }

    private PostLike insert(PostLike postLike) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(postLike);
        var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return PostLike.builder()
                .id(id)
                .memberId(postLike.getMemberId())
                .postId(postLike.getPostId())
                .createdAt(postLike.getCreatedAt())
                .build();
    }
}
