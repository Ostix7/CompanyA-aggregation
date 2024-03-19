package company.a.charlee.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

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
}