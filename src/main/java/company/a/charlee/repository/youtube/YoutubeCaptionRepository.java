package company.a.charlee.repository.youtube;

import company.a.charlee.entity.dto.TopicInfoDTO;
import company.a.charlee.entity.dto.TopicPhraseInfoDTO;
import company.a.charlee.entity.youtube.YoutubeCaption;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface YoutubeCaptionRepository extends JpaRepository<YoutubeCaption, Long> {
    @Query("SELECT yc.youtubeVideo, COUNT(yc) FROM YoutubeCaption yc WHERE yc.language = :language GROUP BY yc.youtubeVideo")
    List<Object[]> countCaptionsByVideoForLanguage(@Param("language") String language);

    @Query("SELECT yc.language, COUNT(yc) FROM YoutubeCaption yc GROUP BY yc.language ORDER BY COUNT(yc) DESC")
    List<Object[]> countCaptionsByLanguage();

    YoutubeCaption findById(String id);

    YoutubeCaption findByYoutubeVideoId(String id);

    @Query("SELECT c FROM YoutubeCaption c WHERE c.youtubeVideo.id = :videoId")
    Optional<List<YoutubeCaption>> findByVideoId(@Param("videoId") String videoId);

    @Query("SELECT t, COUNT(DISTINCT c.youtubeVideo) AS videoCount " +
            "FROM YoutubeCaption c JOIN c.topics t " +
            "GROUP BY t " +
            "ORDER BY videoCount DESC")
    List<Object[]> findMostPopularTopicsByVideoCount();

    @Query("SELECT AVG(c.sentimentValue) FROM YoutubeCaption c JOIN c.youtubeVideo v " +
            "WHERE v.publishedAt BETWEEN :startTimestamp AND :endTimestamp " +
            "AND c.sentimentValue IS NOT NULL")
    Double findAverageSentimentBetweenTimestamps(
            @Param("startTimestamp") long startTimestamp,
            @Param("endTimestamp") long endTimestamp);

    @Query("SELECT new company.a.charlee.entity.dto.TopicInfoDTO(t, c.language, COUNT(t)) FROM YoutubeCaption c " +
            "JOIN c.topics t " +
            "WHERE c.youtubeVideo.publishedAt BETWEEN :startTimestamp AND :endTimestamp " +
            "AND (:channelId IS NULL OR c.youtubeVideo.youtubeChannel.id = :channelId) " +
            "AND (:channelName IS NULL OR c.youtubeVideo.youtubeChannel.title = :channelName) " +
            "AND (:language IS NULL OR c.language = :language) " +
            "GROUP BY t, c.language " +
            "ORDER BY count(t) DESC")
    List<TopicInfoDTO> findMostPopularTopicsInDateRange(
            @Param("channelId") String channelId,
            @Param("channelName") String channelName,
            @Param("language") String language,
            @Param("startTimestamp") long startTimestamp,
            @Param("endTimestamp") long endTimestamp,
            Pageable pageable);

    @Query("SELECT new company.a.charlee.entity.dto.TopicPhraseInfoDTO(t, c.language, COUNT(t)) FROM YoutubeCaption c " +
            "JOIN c.phraseTopics t " +
            "WHERE c.youtubeVideo.publishedAt BETWEEN :startTimestamp AND :endTimestamp " +
            "AND (:channelId IS NULL OR c.youtubeVideo.youtubeChannel.id = :channelId) " +
            "AND (:channelName IS NULL OR c.youtubeVideo.youtubeChannel.title = :channelName) " +
            "AND (:language IS NULL OR c.language = :language) " +
            "GROUP BY t, c.language " +
            "ORDER BY count(t) DESC")
    List<TopicPhraseInfoDTO> findMostPopularTopicPhrasesInDateRange(
            @Param("channelId") String channelId,
            @Param("channelName") String channelName,
            @Param("language") String language,
            @Param("startTimestamp") long startTimestamp,
            @Param("endTimestamp") long endTimestamp,
            Pageable pageable);
}