package company.a.charlee.repository.youtube;

import company.a.charlee.entity.telegram.TelegramComment;
import company.a.charlee.entity.youtube.YoutubeComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YoutubeCommentRepository extends JpaRepository<YoutubeComment, Long> {

    @Query("SELECT yc.youtubeVideo, COUNT(yc) FROM YoutubeComment yc GROUP BY yc.youtubeVideo")
    List<Object[]> countCommentsByVideo();

    @Query("SELECT yc.authorName, COUNT(yc) FROM YoutubeComment yc GROUP BY yc.authorName ORDER BY COUNT(yc) DESC")
    List<Object[]> findTopCommenters(Pageable pageable);

    YoutubeComment findByYoutubeCommentId(String commentId);

    @Query("SELECT c FROM YoutubeComment c JOIN c.youtubeVideo v WHERE v.youtubeChannel.id = :channelId")
    List<YoutubeComment> findByChannelId(@Param("channelId") String channelId);

    @Query("SELECT c FROM YoutubeComment c WHERE c.youtubeVideo.id = :videoId")
    Optional<List<YoutubeComment>> findByVideoId(@Param("videoId") String videoId);
}