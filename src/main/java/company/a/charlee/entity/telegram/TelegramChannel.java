package company.a.charlee.entity.telegram;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TelegramChannel {
    @Id
    private Long channelId;
    private String channelTitle;
}