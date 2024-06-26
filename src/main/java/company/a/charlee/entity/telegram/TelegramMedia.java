package company.a.charlee.entity.telegram;

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
public class TelegramMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String mediaId;

    @ManyToOne
    private TelegramPost post;

    private String mediaType;
    private String mediaDuration;
}