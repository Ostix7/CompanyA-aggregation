package company.a.charlee.repository.youtube;

import company.a.charlee.entity.telegram.TelegramChannel;
import company.a.charlee.entity.youtube.YoutubeCaption;
import company.a.charlee.entity.youtube.YoutubeChannel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.lang.management.LockInfo;
import java.util.List;

@Repository
public interface YoutubeChannelRepository extends JpaRepository<YoutubeChannel, Long> {
    @Query("SELECT yc FROM YoutubeChannel yc ORDER BY yc.subscribersCount DESC")
    List<YoutubeChannel> findTopChannelsBySubscribersCount(Pageable pageable);

    YoutubeChannel findByYoutubeChannelId(String id);
}