package company.a.charlee.entity.telegram;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class TelegramChannel {
    @Id
    private Long channelId;
    private String channelTitle;

    @OneToMany(mappedBy = "channel")
    private List<TelegramPost> posts;
}