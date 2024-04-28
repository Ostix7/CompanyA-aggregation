package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostTopicModelingOccurrencesDTO {

    private LocalDate postDate;

    private String topic;

    private Integer occurrences;

}
