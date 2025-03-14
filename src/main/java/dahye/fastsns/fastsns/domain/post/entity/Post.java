package dahye.fastsns.fastsns.domain.post.entity;

import dahye.fastsns.fastsns.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long memberId;

    @Column
    private String contents;

    @Column
    private LocalDate createdDate;

    @Column
    private Long likeCount;

    @Column
    private Long version;

    @Builder
    public Post(Long id, Long memberId, String contents, LocalDate createdDate, Long likeCount, Long version) {
        this.id = id;
        this.memberId = Objects.requireNonNull(memberId);
        this.contents = Objects.requireNonNull(contents);
        this.createdDate = createdDate == null ? LocalDate.now() : createdDate;
        this.likeCount = likeCount == null ? 0 : likeCount;
        this.version = version == null ? 0 : version;
    }

    public void incrementLikeCount() {
        this.likeCount += 1;
    }
}
