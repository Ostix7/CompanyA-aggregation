package company.a.charlee.entity.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class VideoStatisticsDTO {
    private Long likes;
    private int commentCount;
    private int captionCount;
}
