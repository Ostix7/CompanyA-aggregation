package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YoutubeChannelDTO {
    private String youtubeChannelId;
    private String title;
    private Long subscribersCount;
}
