package company.a.charlee.repository.youtube;

import company.a.charlee.entity.dto.YoutubeChannelDTO;
import company.a.charlee.entity.youtube.YoutubeChannel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YoutubeChannelRepository extends JpaRepository<YoutubeChannel, Long> {
    @Query("SELECT yc FROM YoutubeChannel yc ORDER BY yc.subscribersCount DESC")
    List<YoutubeChannel> findTopChannelsBySubscribersCount(Pageable pageable);

    YoutubeChannel findByYoutubeChannelId(String id);

    @Query(value = "SELECT * FROM youtube_channels yc WHERE yc.youtube_channel_id = :channelId ORDER BY yc.insertion_time DESC LIMIT 1", nativeQuery = true)
    Optional<YoutubeChannel> findLatestByChannelId(String channelId);

    @Query("SELECT new company.a.charlee.entity.dto.YoutubeChannelDTO(c.youtubeChannelId, MAX(c.title), MAX(c.subscribersCount)) " +
            "FROM YoutubeChannel c GROUP BY c.youtubeChannelId ORDER BY MAX(c.subscribersCount) DESC")
    List<YoutubeChannelDTO> findAllChannelNames();
}