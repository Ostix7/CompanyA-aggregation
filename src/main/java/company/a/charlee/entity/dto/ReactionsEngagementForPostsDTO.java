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
public class ReactionsEngagementForPostsDTO {

    private LocalDate postDate;

    private Integer totalReactions;

}
