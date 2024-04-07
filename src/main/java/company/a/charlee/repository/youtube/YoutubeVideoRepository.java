package company.a.charlee.repository.youtube;

import company.a.charlee.entity.telegram.TelegramComment;
import company.a.charlee.entity.youtube.YoutubeVideo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YoutubeVideoRepository extends JpaRepository<YoutubeVideo, Long> {
    List<YoutubeVideo> findByIsProcessedFalse();
//    @Query("SELECT yv.youtubeVideoId, SUM(yv.likes) FROM YoutubeVideo yv GROUP BY yv.youtubeVideoId ORDER BY SUM(yv.likes) DESC")
//    List<Object[]> findMostLikedVideos(Pageable pageable);
//
//    @Query("SELECT yv.youtubeChannel, COUNT(yv) FROM YoutubeVideo yv GROUP BY yv.youtubeChannel")
//    List<Object[]> countVideosByChannel();
//
//    @Query("SELECT yv.youtubeChannel, AVG(yv.likes) FROM YoutubeVideo yv GROUP BY yv.youtubeChannel")
//    List<Object[]> averageLikesByChannel();
//
//    @Query("SELECT yv.tags, COUNT(yv) FROM YoutubeVideo yv JOIN yv.tags t GROUP BY t")
//    List<Object[]> countVideosByTag();
//
//    @Query("SELECT FUNCTION('date', yv.publishedAt), COUNT(yv) FROM YoutubeVideo yv GROUP BY FUNCTION('date', yv.publishedAt)")
//    List<Object[]> countVideosByPublishedDate();
}