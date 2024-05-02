package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActiveCommenterDTO {
    private String commenterName;
    private Long commentCount;
}
