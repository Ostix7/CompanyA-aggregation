package company.a.charlee.repository.youtube;

import company.a.charlee.entity.dto.ActiveCommenterDTO;
import company.a.charlee.entity.dto.YoutubeCommentDTO;
import company.a.charlee.entity.dto.YoutubePopularCommentDTO;
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

    @Query("SELECT new company.a.charlee.entity.dto.ActiveCommenterDTO(c.authorName, COUNT(c)) " +
            "FROM YoutubeComment c JOIN c.youtubeVideo v JOIN v.youtubeChannel ch " +
            "WHERE (:channelId IS NULL OR ch.id = :channelId) AND " +
            "(:channelName IS NULL OR ch.title = :channelName) AND " +
            "(c.publishedAt BETWEEN :startTimestamp AND :endTimestamp) " +
            "GROUP BY c.authorName " +
            "ORDER BY COUNT(c) DESC")
    List<ActiveCommenterDTO> findActiveCommentersByChannel(
            @Param("channelId") String channelId,
            @Param("channelName") String channelName,
            @Param("startTimestamp") long startTimestamp,
            @Param("endTimestamp") long endTimestamp,
            Pageable pageable);

    @Query("SELECT new company.a.charlee.entity.dto.YoutubeCommentDTO(c.authorName, c.text, c.likes) " +
            "FROM YoutubeComment c JOIN c.youtubeVideo v " +
            "WHERE (v.youtubeVideoId = :videoId) " +
            "ORDER BY c.likes DESC")
    List<YoutubeCommentDTO> findMostLikedCommentsForVideo(
            @Param("videoId") String videoId,
            Pageable pageable);

    @Query("SELECT new company.a.charlee.entity.dto.YoutubePopularCommentDTO(c.authorName, c.text, c.likes, c.publishedAt, v.youtubeVideoId, v.title, v.publishedAt) " +
            "FROM YoutubeComment c JOIN c.youtubeVideo v JOIN v.youtubeChannel ch " +
            "WHERE (:channelId IS NULL OR ch.id = :channelId) AND " +
            "(:channelName IS NULL OR ch.title = :channelName) AND " +
            "(c.publishedAt BETWEEN :startTimestamp AND :endTimestamp) " +
            "ORDER BY c.likes DESC")
    List<YoutubePopularCommentDTO> findMostLikedCommentsInDateRange(
            @Param("channelId") String channelId,
            @Param("channelName") String channelName,
            @Param("startTimestamp") long startTimestamp,
            @Param("endTimestamp") long endTimestamp,
            Pageable pageable);
}