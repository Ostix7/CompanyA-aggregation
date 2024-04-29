package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class YoutubeCaptionDTO {
    private String id;
    private String language;
    private String content;
}
