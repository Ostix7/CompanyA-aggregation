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
public class TelegramReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reactionId;

    @ManyToOne
    private TelegramPost post;

    private String emoji;
    private Integer count;
}