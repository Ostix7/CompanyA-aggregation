package company.a.charlee.repository.telegram;

import company.a.charlee.entity.telegram.TelegramReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramReactionRepository extends JpaRepository<TelegramReaction, Long> {
    @Query("SELECT tr.emoji, SUM(tr.count) FROM TelegramReaction tr GROUP BY tr.emoji ORDER BY SUM(tr.count) DESC")
    List<Object[]> findMostPopularEmojis();

    @Query("SELECT tr.post.id, SUM(tr.count) FROM TelegramReaction tr GROUP BY tr.post.id")
    List<Object[]> countTotalReactionsByPost();
}