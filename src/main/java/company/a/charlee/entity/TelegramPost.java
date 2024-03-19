package company.a.charlee.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class TelegramPost {
    @Id
    private String id;
    private Integer schemaVersion;

    @ManyToOne
    @JoinColumn(name = "channel_id", referencedColumnName = "channelId")
    private TelegramChannel channel;

    private Long telegramPostId;
    private String postDate;
    private Long postTs;
    private java.time.Instant updatedAt;
    private String lang;
    private String segment;
    private String fullText;
    private Integer viewCount;

    @OneToMany(mappedBy = "post")
    private List<TelegramMedia> media;

    @OneToMany(mappedBy = "post")
    private List<TelegramComment> comments;

    @OneToMany(mappedBy = "post")
    private List<TelegramReaction> reactions;
}