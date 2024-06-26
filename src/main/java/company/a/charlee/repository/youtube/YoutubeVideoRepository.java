package company.a.charlee.repository.youtube;

import company.a.charlee.entity.youtube.YoutubeVideo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YoutubeVideoRepository extends JpaRepository<YoutubeVideo, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM youtube_videos WHERE is_processed = FALSE LIMIT 100")
    List<YoutubeVideo> get100NotProcessedVideos();
    YoutubeVideo findByYoutubeVideoId(String id);

    @Query("SELECT v FROM YoutubeVideo v WHERE v.youtubeChannel.id = :channelId")
    Optional<List<YoutubeVideo>> findByChannelId(@Param("channelId") String channelId);

    @Query("SELECT v FROM YoutubeVideo v ORDER BY v.likes DESC")
    List<YoutubeVideo> findMostLikedVideos(Pageable pageable);

    @Query("SELECT COUNT(v) FROM YoutubeVideo v WHERE v.isProcessed = true")
    Long countByIsProcessedTrue();

    @Query(value = "SELECT * FROM youtube_videos yv WHERE yv.youtube_video_id = :videoId ORDER BY yv.insertion_time DESC LIMIT 1", nativeQuery = true)
    Optional<YoutubeVideo> findLatestByVideoId(String videoId);

    @Query("SELECT v FROM YoutubeVideo v WHERE " +
            "(:channelId IS NULL OR v.youtubeChannel.id = :channelId) AND " +
            "(:channelName IS NULL OR v.youtubeChannel.title = :channelName) AND " +
            "v.publishedAt BETWEEN :startTimestamp AND :endTimestamp " +
            "ORDER BY v.likes DESC")
    List<YoutubeVideo> findMostLikedByDateRangeAndChannel(
            @Param("channelId") String channelId,
            @Param("channelName") String channelName,
            @Param("startTimestamp") long startTimestamp,
            @Param("endTimestamp") long endTimestamp,
            Pageable pageable);
}