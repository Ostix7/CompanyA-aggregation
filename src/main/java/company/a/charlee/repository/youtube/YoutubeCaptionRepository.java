package company.a.charlee.repository.youtube;

import company.a.charlee.entity.youtube.YoutubeCaption;
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


}