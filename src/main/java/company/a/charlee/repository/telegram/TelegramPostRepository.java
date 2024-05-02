package company.a.charlee.repository.telegram;

import company.a.charlee.entity.dto.ChannelPostsQuantityDTO;
import company.a.charlee.entity.dto.CommentsEngagementForPostsDTO;
import company.a.charlee.entity.dto.ReactionsEngagementForPostsDTO;
import company.a.charlee.entity.dto.TotalPostsViewsDTO;
import company.a.charlee.entity.telegram.TelegramPost;
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

    @Query(nativeQuery = true, value =
            "SELECT to_timestamp(:minDate + (DIV(EXTRACT(EPOCH FROM DATE_TRUNC(:timeFrame, t1.post_date)) - :minDate, :interval) * :interval)) AS aggregated_date, AVG(t1.sentiment_value)\n" +
            "FROM telegram_post t1 JOIN telegram_channel t2 ON t1.channel_id = t2.channel_id\n" +
            "WHERE (EXTRACT(EPOCH FROM t1.post_date) BETWEEN :minDate AND :maxDate) AND t2.channel_title = :telegramChannelTitle AND t1.is_processed = TRUE\n" +
            "GROUP BY aggregated_date\n" +
            "ORDER BY aggregated_date\n")
    List<Object[]> getAverageSentimentValuesForChannelPostsByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle,
            @Param("timeFrame") String timeFrame,
            @Param("interval") long interval,
            @Param("minDate") long minDate,
            @Param("maxDate") long maxDate
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
            "SELECT aggregatedDate AS aggregatedDate, word AS topic, occurrences AS occurrences " +
            "FROM (\n" +
            "    SELECT aggregatedDate, " +
            "           word, " +
            "           COUNT(word) AS occurrences, \n" +
            "           ROW_NUMBER() OVER (PARTITION BY aggregatedDate ORDER BY COUNT(word) DESC) AS rn\n" +
            "    FROM (\n" +
            "        SELECT to_timestamp(:minDate + (DIV(EXTRACT(EPOCH FROM DATE_TRUNC(:timeFrame, t2.post_date)) - :minDate, :interval) * :interval)) AS aggregatedDate, \n" +
            "               lower(t1.topic_modeling) AS word\n" +
            "        FROM telegram_post_topics t1\n" +
            "        JOIN telegram_post t2 ON t1.telegram_post_id = t2.id\n" +
            "        JOIN telegram_channel t3 ON t2.channel_id = t3.channel_id\n" +
            "        WHERE EXTRACT(EPOCH FROM t2.post_date) BETWEEN :minDate AND :maxDate AND t3.channel_title = :telegramChannelTitle\n" +
            "    ) AS base_data\n" +
            "    GROUP BY aggregatedDate, word\n" +
            ") AS ranked_words\n" +
            "WHERE rn <= :topicsLimitByDate\n" +
            "ORDER BY aggregatedDate, occurrences DESC;\n")
    List<Object[]> getPostTopicModelingOccurrencesByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle,
            @Param("topicsLimitByDate") Integer topicsLimitByDate,
            @Param("timeFrame") String timeFrame,
            @Param("interval") long interval,
            @Param("minDate") long minDate,
            @Param("maxDate") long maxDate
    );

    @Query(nativeQuery = true, value =
            "SELECT aggregatedDate AS aggregatedDate, word AS topic, occurrences AS occurrences " +
                    "FROM (\n" +
                    "    SELECT aggregatedDate, " +
                    "           word, " +
                    "           COUNT(word) AS occurrences, \n" +
                    "           ROW_NUMBER() OVER (PARTITION BY aggregatedDate ORDER BY COUNT(word) DESC) AS rn\n" +
                    "    FROM (\n" +
                    "        SELECT to_timestamp(:minDate + (DIV(EXTRACT(EPOCH FROM DATE_TRUNC(:timeFrame, t2.post_date)) - :minDate, :interval) * :interval)) AS aggregatedDate, \n" +
                    "               lower(t1.topic_modeling_phrases) AS word\n" +
                    "        FROM telegram_post_phrase_topics t1\n" +
                    "        JOIN telegram_post t2 ON t1.telegram_post_id = t2.id\n" +
                    "        JOIN telegram_channel t3 ON t2.channel_id = t3.channel_id\n" +
                    "        WHERE EXTRACT(EPOCH FROM t2.post_date) BETWEEN :minDate AND :maxDate AND t3.channel_title = :telegramChannelTitle\n" +
                    "    ) AS base_data\n" +
                    "    GROUP BY aggregatedDate, word\n" +
                    ") AS ranked_words\n" +
                    "WHERE rn <= :topicsLimitByDate\n" +
                    "ORDER BY aggregatedDate, occurrences DESC;\n")
    List<Object[]> getPostPhraseTopicModelingOccurrencesByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle,
            @Param("topicsLimitByDate") Integer topicsLimitByDate,
            @Param("timeFrame") String timeFrame,
            @Param("interval") long interval,
            @Param("minDate") long minDate,
            @Param("maxDate") long maxDate
    );

    @Query(nativeQuery = true, value =
            "SELECT to_timestamp(:minDate + (DIV(EXTRACT(EPOCH FROM DATE_TRUNC(:timeFrame, t2.post_date)) - :minDate, :interval) * :interval)) AS aggregated_date, COUNT(t1.comment_id)\n" +
            "FROM telegram_comment t1 JOIN telegram_post t2 ON t1.post_id = t2.id JOIN telegram_channel t3 ON t2.channel_id = t3.channel_id \n" +
            "WHERE (EXTRACT(EPOCH FROM t2.post_date) BETWEEN :minDate AND :maxDate) AND t3.channel_title = :telegramChannelTitle \n" +
            "GROUP BY aggregated_date\n" +
            "ORDER BY aggregated_date\n")
    List<Object[]> getCommentsEngagementForPostsByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle,
            @Param("timeFrame") String timeFrame,
            @Param("interval") long interval,
            @Param("minDate") long minDate,
            @Param("maxDate") long maxDate
    );

    @Query(nativeQuery = true, value =
            "SELECT to_timestamp(:minDate + (DIV(EXTRACT(EPOCH FROM DATE_TRUNC(:timeFrame, t2.post_date)) - :minDate, :interval) * :interval)) AS aggregated_date, SUM(t1.count)\n" +
            "FROM telegram_reaction t1 JOIN telegram_post t2 ON t1.post_id = t2.id JOIN telegram_channel t3 ON t2.channel_id = t3.channel_id \n" +
            "WHERE (EXTRACT(EPOCH FROM t2.post_date) BETWEEN :minDate AND :maxDate) AND t3.channel_title = :telegramChannelTitle \n" +
            "GROUP BY aggregated_date\n" +
            "ORDER BY aggregated_date\n")
    List<Object[]> getReactionsEngagementForPostsByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle,
            @Param("timeFrame") String timeFrame,
            @Param("interval") long interval,
            @Param("minDate") long minDate,
            @Param("maxDate") long maxDate
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