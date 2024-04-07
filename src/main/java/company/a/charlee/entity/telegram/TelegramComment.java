package company.a.charlee.entity.telegram;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class TelegramComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @ManyToOne
    private TelegramPost post;

    private Long telegramMessageId;
    private Long senderId;
    private String senderUsername;
    private String senderFirstName;
    private String senderLastName;
    private Long replyTo;
    private String fullText;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<TelegramReaction> reactions = new ArrayList<>();

    public void addReaction(TelegramReaction reaction) {
        if (this.reactions == null) {
            this.reactions = new ArrayList<>();
        }
        this.reactions.add(reaction);
        reaction.setComment(this);
    }
}