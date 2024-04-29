package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YoutubeCommentDTO {
    private String id;
    private String text;
    private Long likes;
    private String authorName;
}

