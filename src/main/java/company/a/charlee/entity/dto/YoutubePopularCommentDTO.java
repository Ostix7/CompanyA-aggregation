package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YoutubePopularCommentDTO {
    private String authorName;
    private String text;
    private Long likes;
    private Long commentPublishedAt;
    private String videoId;
    private String videoTitle;
    private Long videoPublishedAt;
}

