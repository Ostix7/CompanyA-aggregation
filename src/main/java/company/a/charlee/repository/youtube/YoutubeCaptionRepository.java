package company.a.charlee.repository.youtube;

import company.a.charlee.entity.youtube.YoutubeCaption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface YoutubeCaptionRepository extends JpaRepository<YoutubeCaption, Long> {
    @Query("SELECT yc.youtubeVideo, COUNT(yc) FROM YoutubeCaption yc WHERE yc.language = :language GROUP BY yc.youtubeVideo")
    List<Object[]> countCaptionsByVideoForLanguage(@Param("language") String language);

    @Query("SELECT yc.language, COUNT(yc) FROM YoutubeCaption yc GROUP BY yc.language ORDER BY COUNT(yc) DESC")
    List<Object[]> countCaptionsByLanguage();

    YoutubeCaption findById(String id);

    YoutubeCaption findByYoutubeVideoId(String id);
}