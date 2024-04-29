package company.a.charlee.entity.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelStatisticsDTO {
    private String channelId;
    private String title;
    private long subscribersCount;
    private int totalVideos;
}
