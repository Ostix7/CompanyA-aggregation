package company.a.charlee.repository.telegram;

import company.a.charlee.entity.telegram.TelegramPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramPostRepository extends JpaRepository<TelegramPost, String> {
    List<TelegramPost> findByProcessedFalse();
    @Query("SELECT tp.channel.channelTitle, AVG(tp.viewCount) FROM TelegramPost tp GROUP BY tp.channel.channelTitle")
    List<Object[]> avgViewCountByChannel();

    List<TelegramPost> findTopByOrderByViewCountDesc(Pageable pageable);

    @Query("SELECT FUNCTION('date', tp.postDate), COUNT(tp) FROM TelegramPost tp GROUP BY FUNCTION('date', tp.postDate)")
    List<Object[]> countPostsByDay();

    @Query("SELECT p FROM TelegramPost p JOIN p.reactions r GROUP BY p ORDER BY SUM(r.count) DESC")
    List<TelegramPost> findTopPostsByTotalReactions(Pageable pageable);

    @Query("SELECT p, COUNT(DISTINCT r.emoji) FROM TelegramPost p JOIN p.reactions r GROUP BY p ORDER BY COUNT(DISTINCT r.emoji) DESC")
    List<Object[]> findPostsWithMostDiverseEmojis(Pageable pageable);

}