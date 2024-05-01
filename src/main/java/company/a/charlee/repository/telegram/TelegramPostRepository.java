package company.a.charlee.repository.telegram;

import company.a.charlee.entity.dto.*;
import company.a.charlee.entity.telegram.TelegramPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramPostRepository extends JpaRepository<TelegramPost, String> {

    @Query(nativeQuery = true, value = "SELECT * FROM telegram_post WHERE is_processed = FALSE LIMIT 100")
    List<TelegramPost> get100NotProcessedPosts();
//    @Query("SELECT tp.channel.channelTitle, AVG(tp.viewCount) FROM TelegramPost tp GROUP BY tp.channel.channelTitle")
//    List<Object[]> avgViewCountByChannel();
//
//    List<TelegramPost> findTopByOrderByViewCountDesc(Pageable pageable);
//
//    @Query("SELECT DATE_TRUNC('day', CAST(tp.postDate AS TIMESTAMP)), COUNT(tp) FROM TelegramPost tp GROUP BY DATE_TRUNC('day', CAST(tp.postDate AS TIMESTAMP))")
//    List<Object[]> countPostsByDay();
//    @Query("SELECT p FROM TelegramPost p JOIN p.reactions r GROUP BY p ORDER BY SUM(r.count) DESC")
//    List<TelegramPost> findTopPostsByTotalReactions(Pageable pageable);
//
//    @Query("SELECT p, COUNT(DISTINCT r.emoji) FROM TelegramPost p JOIN p.reactions r GROUP BY p ORDER BY COUNT(DISTINCT r.emoji) DESC")
//    List<Object[]> findPostsWithMostDiverseEmojis(Pageable pageable);

    @Query(value =
            "SELECT new company.a.charlee.entity.dto.AveragePostsSentimentValueDTO(CAST(t1.postDate AS java.time.LocalDate), AVG(t1.sentimentValue))\n" +
            "FROM TelegramPost t1\n" +
            "WHERE t1.channel.channelTitle = :telegramChannelTitle AND t1.isProcessed = TRUE\n" +
            "GROUP BY CAST(t1.postDate AS date)\n" +
            "ORDER BY CAST(t1.postDate AS date)\n")
    List<AveragePostsSentimentValueDTO> getAverageSentimentValuesForChannelPostsByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle
    );

    @Query(value =
            "SELECT new company.a.charlee.entity.dto.TotalPostsViewsDTO(CAST(t1.postDate AS java.time.LocalDate), SUM(t1.viewCount))\n" +
                    "FROM TelegramPost t1\n" +
                    "WHERE t1.channel.channelTitle = :telegramChannelTitle\n" +
                    "GROUP BY CAST(t1.postDate AS date)\n" +
                    "ORDER BY CAST(t1.postDate AS date), SUM(t1.viewCount) DESC\n")
    List<TotalPostsViewsDTO> getTotalPostsViewsByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle
    );

    @Query(nativeQuery = true, value =
            "SELECT post_date AS postDate, word AS topic, occurrences AS occurrences\n" +
            "FROM (\n" +
            "    SELECT \n" +
            "        CAST(t2.post_date AS date) AS post_date, \n" +
            "        lower(t1.topic_modeling) as word, \n" +
            "        count(lower(t1.topic_modeling)) as occurrences,\n" +
            "        ROW_NUMBER() OVER (PARTITION BY CAST(t2.post_date AS date) ORDER BY count(lower(t1.topic_modeling)) DESC) as rn\n" +
            "    FROM telegram_post_topics t1 \n" +
            "    JOIN telegram_post t2 ON t1.telegram_post_id = t2.id \n" +
            "    JOIN telegram_channel t3 ON t2.channel_id = t3.channel_id \n" +
            "    WHERE t3.channel_title = :telegramChannelTitle\n" +
            "    GROUP BY CAST(t2.post_date AS date), lower(t1.topic_modeling)\n" +
            ") ranked_words\n" +
            "WHERE rn <= :topicsLimitByDate\n" +
            "ORDER BY post_date, occurrences DESC;\n")
    List<Object[]> getPostTopicModelingOccurrencesByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle,
            @Param("topicsLimitByDate") Integer topicsLimitByDate
    );

    @Query(value =
            "SELECT new company.a.charlee.entity.dto.CommentsEngagementForPostsDTO(CAST(t2.postDate AS java.time.LocalDate), COUNT(t1.commentId))\n" +
            "FROM TelegramComment t1 JOIN t1.post t2 JOIN t2.channel t3 \n" +
            "WHERE t3.channelTitle = :telegramChannelTitle \n" +
            "GROUP BY CAST(t2.postDate AS date)\n" +
            "ORDER BY CAST(t2.postDate AS date)\n")
    List<CommentsEngagementForPostsDTO> getCommentsEngagementForPostsByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle
    );

    @Query(value =
            "SELECT new company.a.charlee.entity.dto.ReactionsEngagementForPostsDTO(CAST(t2.postDate AS java.time.LocalDate), SUM(t1.count))\n" +
            "FROM TelegramReaction t1 JOIN t1.post t2 JOIN t2.channel t3 \n" +
            "WHERE t3.channelTitle = :telegramChannelTitle \n" +
            "GROUP BY CAST(t2.postDate AS date)\n" +
            "ORDER BY CAST(t2.postDate AS date)\n")
    List<ReactionsEngagementForPostsDTO> getReactionsEngagementForPostsByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle
    );

    @Query(value =
            "SELECT new company.a.charlee.entity.dto.ChannelPostsQuantityDTO(c.channelTitle, SIZE(c.posts))\n" +
            "FROM TelegramChannel c")
    List<ChannelPostsQuantityDTO> getAllChannelPostsQuantity();

    @Query(value =
            "SELECT new company.a.charlee.entity.dto.ChannelPostsQuantityDTO(c.channelTitle, SIZE(c.posts))\n" +
            "FROM TelegramChannel c \n" +
            "WHERE c.channelTitle = :telegramChannelTitle\n")
    ChannelPostsQuantityDTO getChannelPostsQuantity(
            @Param("telegramChannelTitle") String telegramChannelTitle
    );

}