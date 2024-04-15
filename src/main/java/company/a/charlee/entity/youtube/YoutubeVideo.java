package company.a.charlee.entity.youtube;
import company.a.charlee.entity.generic.SentimentValuedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "youtube_videos")
@Getter
@Setter
public class YoutubeVideo extends SentimentValuedEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "youtube_video_id")
    private String youtubeVideoId;

    private String title;

    private String description;

    private Long likes;

    @ElementCollection
    @CollectionTable(name = "video_tags", joinColumns = @JoinColumn(name = "video_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "youtube_video_topics")
    @Column(name = "topic_modeling")
    private List<String> topics;

    @OneToMany(mappedBy = "youtubeVideo", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<YoutubeCaption> captions = new ArrayList<>();

    @Column(name = "published_at")
    private Long publishedAt;

    @Column(name = "insertion_time")
    private Long insertionTime;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private YoutubeChannel youtubeChannel;

    @Column(name = "is_processed")
    private Boolean isProcessed = false;

    public void addTag(String tag) {
        this.tags.add(tag);
    }
}