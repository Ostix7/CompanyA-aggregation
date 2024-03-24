package company.a.charlee.repository.telegram;

import company.a.charlee.entity.telegram.TelegramComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramCommentRepository extends JpaRepository<TelegramComment, Long> {
}