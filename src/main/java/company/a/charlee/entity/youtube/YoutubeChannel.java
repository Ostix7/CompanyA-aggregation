package company.a.charlee.entity.youtube;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "youtube_channels")
@Getter
@Setter
public class YoutubeChannel {
    @Id
    private String id;

    @Column(name = "youtube_channel_id")
    private String youtubeChannelId;

    private String title;

    @Column(name = "subscribers_count")
    private Long subscribersCount;

    @ElementCollection
    @CollectionTable(name = "channel_video_ids", joinColumns = @JoinColumn(name = "channel_id"))
    @Column(name = "video_id")
    private List<String> videoIds = new ArrayList<>();

    @Column(name = "insertion_time")
    private Long insertionTime;
}