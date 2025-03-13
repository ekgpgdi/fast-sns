package dahye.fastsns.fastsns.domain.post.repository;

import dahye.fastsns.fastsns.util.PageHelper;
import dahye.fastsns.fastsns.domain.post.dto.DailyPostCount;
import dahye.fastsns.fastsns.domain.post.dto.DailyPostCountRequest;
import dahye.fastsns.fastsns.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PostRepository {
    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    static final private String TABLE = "POST";

    static final RowMapper<Post> rowMapper = (ResultSet resultSet, int rowNum)
            -> Post.builder()
            .id(resultSet.getLong("id"))
            .memberId(resultSet.getLong("memberId"))
            .contents(resultSet.getString("contents"))
            .createdDate(resultSet.getObject("createdDate", LocalDate.class))
            .likeCount(resultSet.getLong("likeCount"))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();

    static final RowMapper<DailyPostCount> DAILY_POST_COUNT_ROW_MAPPER = (ResultSet resultSet, int rowNum)
            -> new DailyPostCount(resultSet.getLong("memberId"),
            resultSet.getObject("createdDate", LocalDate.class),
            resultSet.getLong("count"));

    public List<DailyPostCount> groupByCreatedDate(DailyPostCountRequest request) {
        var sql = String.format("""
                    SELECT createdDate, memberId, count(id) as count
                    FROM %s
                    WHERE memberId = :memberId AND createdDate BETWEEN :firstDate AND :lastDate
                    GROUP BY memberId, createdDate
                """, TABLE);
        var params = new BeanPropertySqlParameterSource(request);
        return namedParameterJdbcTemplate.query(sql, params, DAILY_POST_COUNT_ROW_MAPPER);
    }

    public Page<Post> findAllByMemberId(Long memberId, Pageable pageable) {
        var sql = String.format(""" 
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY %s
                LIMIT :size
                OFFSET :offset
                """, TABLE, PageHelper.orderBy(pageable.getSort()));

        var param = new MapSqlParameterSource().addValue("memberId", memberId)
                .addValue("size", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        var posts = namedParameterJdbcTemplate.query(sql, param, rowMapper);

        return new PageImpl<>(posts, pageable, getCount(memberId));
    }

    public Optional<Post> findById(Long postId, Boolean requiredLock) {
        var sql = String.format("SELECT * FROM %s WHERE id = :id", TABLE);

        if(requiredLock) {
            sql += " FOR UPDATE ";
        }
        var param = new MapSqlParameterSource().addValue("id", postId);
        var nullablePost = namedParameterJdbcTemplate.queryForObject(sql, param, rowMapper);
        return Optional.ofNullable(nullablePost);
    }

    private Long getCount(Long memberId) {
        var sql = String.format("SELECT COUNT(*) FROM %s WHERE memberId = :memberId ", TABLE);
        var param = new MapSqlParameterSource().addValue("memberId", memberId);
        return namedParameterJdbcTemplate.queryForObject(sql, param, Long.class);
    }

    public List<Post> findAllByInId(List<Long> ids) {
        if (ids.isEmpty()) return List.of();

        var sql = String.format(""" 
                SELECT *
                FROM %s
                WHERE  id IN (:ids)
                """, TABLE);

        var param = new MapSqlParameterSource()
                .addValue("ids", ids);

        return namedParameterJdbcTemplate.query(sql, param, rowMapper);
    }

    public List<Post> findAllByMemberIdAndOrderByIdDesc(Long memberId, int size) {
        var sql = String.format(""" 
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY id DESC
                LIMIT :size
                """, TABLE);

        var param = new MapSqlParameterSource().addValue("memberId", memberId)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, param, rowMapper);
    }

    public List<Post> findAllByLessThanIdAndMemberIdAndOrderByIdDesc(Long id, Long memberId, int size) {
        var sql = String.format(""" 
                SELECT *
                FROM %s
                WHERE memberId = :memberId AND id < :id
                ORDER BY id DESC
                LIMIT :size
                """, TABLE);

        var param = new MapSqlParameterSource().addValue("memberId", memberId)
                .addValue("size", size)
                .addValue("id", id);

        return namedParameterJdbcTemplate.query(sql, param, rowMapper);
    }

    public List<Post> findAllByInMemberIdAndOrderByIdDesc(List<Long> memberIds, int size) {
        if (memberIds.isEmpty()) return List.of();

        var sql = String.format(""" 
                SELECT *
                FROM %s
                WHERE memberId IN (:memberIds)
                ORDER BY id DESC
                LIMIT :size
                """, TABLE);

        var param = new MapSqlParameterSource().addValue("memberIds", memberIds)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, param, rowMapper);
    }

    public List<Post> findAllByLessThanIdAndInMemberIdAndOrderByIdDesc(Long id, List<Long> memberIds, int size) {
        if (memberIds.isEmpty()) return List.of();

        var sql = String.format(""" 
                SELECT *
                FROM %s
                WHERE memberId IN (:memberIds) AND id < :id
                ORDER BY id DESC
                LIMIT :size
                """, TABLE);

        var param = new MapSqlParameterSource().addValue("memberIds", memberIds)
                .addValue("size", size)
                .addValue("id", id);

        return namedParameterJdbcTemplate.query(sql, param, rowMapper);
    }

    public Post save(Post post) {
        /*
        member id 를 보고 갱신 또는 삽입을 정함
        반환값은 id 를 담아서 반환한다.
         */
        if (post.getId() == null) {
            return insert(post);
        }
        return update(post);
    }

    public void bulkInsert(List<Post> posts) {
        var sql = String.format("INSERT INTO `%s` (memberId, contents, createdDate, createdAt) " +
                "VALUES(:memberId, :contents, :createdDate, :createdAt) ", TABLE);

        SqlParameterSource[] params = posts.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);

        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    private Post insert(Post post) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        var id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return Post.builder()
                .id(id)
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public Post update(Post post) {
        var sql = String.format("""
                UPDATE %s SET
                memberId = :memberId,
                contents = :contents,
                createdDate = :createdDate,
                likeCount = :likeCount,
                createdAt = :createdAt
                WHERE id = :id
                """, TABLE);

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        namedParameterJdbcTemplate.update(sql, params);
        return post;
    }
}
