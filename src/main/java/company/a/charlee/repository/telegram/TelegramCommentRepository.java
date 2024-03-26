package company.a.charlee.repository.telegram;

import company.a.charlee.entity.telegram.TelegramComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramCommentRepository extends JpaRepository<TelegramComment, Long> {
    @Query("SELECT tc.senderUsername, COUNT(tc) FROM TelegramComment tc GROUP BY tc.senderUsername ORDER BY COUNT(tc) DESC")
    List<Object[]> mostActiveUsersByComments();


}