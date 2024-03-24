package company.a.charlee.repository.youtube;

import company.a.charlee.entity.telegram.TelegramChannel;
import company.a.charlee.entity.youtube.YoutubeChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeChannelRepository extends JpaRepository<YoutubeChannel, Long> {
}