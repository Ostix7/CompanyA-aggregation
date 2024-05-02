package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicInfoDTO {
    private String topic;
    private String language;
    private Long topicAppearanceCount;
}
