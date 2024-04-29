package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YoutubeVideoDTO {
    private String id;
    private String title;
    private String description;
    private Long publishedAt;
}