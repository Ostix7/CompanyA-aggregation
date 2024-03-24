package company.a.charlee.repository.youtube;

import company.a.charlee.entity.youtube.YoutubeCaption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeCaptionRepository extends JpaRepository<YoutubeCaption, Long> {
}