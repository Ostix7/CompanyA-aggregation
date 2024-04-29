package company.a.charlee.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChannelPostsQuantityDTO {

    private String channelTitle;

    private Integer totalPosts;

}
