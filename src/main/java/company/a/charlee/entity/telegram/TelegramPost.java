package company.a.charlee.entity.telegram;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
    @Column(columnDefinition = "TEXT")
    private String fullText;
    private Integer viewCount;

    @ElementCollection
    @CollectionTable(name = "telegram_post_topics")
    @Column(name = "topic_modeling")
    private List<String> topics;

    @OneToMany(mappedBy = "post")
    private List<TelegramMedia> media;

    @OneToMany(mappedBy = "post")
    private List<TelegramComment> comments;

    @OneToMany(mappedBy = "post")
    private List<TelegramReaction> reactions;

    @Column(name = "is_processed")
    private boolean isProcessed;

    public void addMedia(TelegramMedia media) {
        if (media != null) {
            if (this.media == null) {
                this.media = new ArrayList<>();
            }
            media.setPost(this);
            this.media.add(media);
        }
    }

    public void addComment(TelegramComment comment) {
        if (comment != null) {
            if (this.comments == null) {
                this.comments = new ArrayList<>();
            }
            comment.setPost(this);
            this.comments.add(comment);
        }
    }

    public void addReaction(TelegramReaction reaction) {
        if (reaction != null) {
            if (this.reactions == null) {
                this.reactions = new ArrayList<>();
            }
            reaction.setPost(this);
            this.reactions.add(reaction);
        }
    }
}
