package company.a.charlee.repository.youtube;

import company.a.charlee.entity.telegram.TelegramComment;
import company.a.charlee.entity.youtube.YoutubeVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YoutubeVideoRepository extends JpaRepository<YoutubeVideo, Long> {
    List<YoutubeVideo> findByIsProcessedFalse();

}