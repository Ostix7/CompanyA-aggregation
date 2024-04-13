package company.a.charlee.entity.youtube;
import company.a.charlee.entity.generic.SentimentValuedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Entity
@Table(name = "youtube_captions")
@Getter
@Setter
public class YoutubeCaption extends SentimentValuedEntity {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "youtube_video_id", referencedColumnName = "youtube_video_id")
    private YoutubeVideo youtubeVideo;

    private String language;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "insertion_time")
    private Long insertionTime;
}