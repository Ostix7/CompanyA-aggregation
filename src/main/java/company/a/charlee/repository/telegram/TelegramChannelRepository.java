package company.a.charlee.repository.telegram;

import company.a.charlee.entity.telegram.TelegramChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramChannelRepository extends JpaRepository<TelegramChannel, Long> {
    @Query("SELECT tc.channelTitle, COUNT(tp) FROM TelegramChannel tc JOIN tc.posts tp GROUP BY tc.channelTitle")
    List<Object[]> countPostsByChannel();
}