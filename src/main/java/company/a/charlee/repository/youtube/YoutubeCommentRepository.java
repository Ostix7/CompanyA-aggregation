package company.a.charlee.repository.youtube;

import company.a.charlee.entity.telegram.TelegramComment;
import company.a.charlee.entity.youtube.YoutubeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YoutubeCommentRepository extends JpaRepository<YoutubeComment, Long> {
}