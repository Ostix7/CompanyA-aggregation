package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicPhraseInfoDTO {
    private String topicPhrase;
    private String language;
    private Long topicPhraseAppearanceCount;
}
