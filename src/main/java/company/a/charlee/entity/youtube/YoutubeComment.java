package company.a.charlee.entity.youtube;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "youtube_comments")
@Getter
@Setter
public class YoutubeComment {
    @Id
    private String id;

    @Column(name = "youtube_comment_id")
    private String youtubeCommentId;

    @ManyToOne
    @JoinColumn(name = "youtube_video_id", referencedColumnName = "youtube_video_id")
    private YoutubeVideo youtubeVideo;

    private String text;

    private Long likes;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "author_profile_image_url")
    private String authorProfileImageUrl;

    @Column(name = "published_at")
    private Long publishedAt;

    @Column(name = "insertion_time")
    private Long insertionTime;
}