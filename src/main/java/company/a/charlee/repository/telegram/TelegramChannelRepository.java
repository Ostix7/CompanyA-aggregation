package company.a.charlee.repository.telegram;

import company.a.charlee.entity.dto.ChannelViewsCountDTO;
import company.a.charlee.entity.dto.TelegramChannelDTO;
import company.a.charlee.entity.telegram.TelegramChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramChannelRepository extends JpaRepository<TelegramChannel, Long> {
    @Query("SELECT tc.channelTitle, COUNT(tp) FROM TelegramChannel tc JOIN tc.posts tp GROUP BY tc.channelTitle")
    List<Object[]> countPostsByChannel();

    @Query(nativeQuery = true,
            value = "SELECT new company.a.charlee.entity.dto.TelegramChannelDTO(tc.channelTitle) FROM TelegramChannel tc LIMIT :limit")
    List<TelegramChannelDTO> getAll(int limit);

    @Query(value =
            "SELECT new company.a.charlee.entity.dto.ChannelViewsCountDTO(t2.channelTitle, SUM(t1.viewCount)) \n" +
            "FROM TelegramPost t1 JOIN t1.channel t2 \n" +
            "GROUP BY t2.channelTitle \n" +
            "ORDER BY SUM(t1.viewCount) DESC\n")
    List<ChannelViewsCountDTO> getChannelsViewsCount();

}