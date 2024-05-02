package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoInfoDTO {
    private String videoTitle;
    private Long likes;
    private String channelName;
    private Date publishedAt;
}
