package company.a.charlee.repository;

import company.a.charlee.entity.TelegramComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramCommentRepository extends JpaRepository<TelegramComment, Long> {
}