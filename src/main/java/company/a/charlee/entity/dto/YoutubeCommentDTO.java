package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeCommentDTO {
    private String authorName;
    private String text;
    private Long likes;
}

