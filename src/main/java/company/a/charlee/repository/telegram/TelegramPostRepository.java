package company.a.charlee.repository.telegram;

import company.a.charlee.entity.dto.AveragePostsSentimentValueDTO;
import company.a.charlee.entity.dto.CommentsEngagementForPostsDTO;
import company.a.charlee.entity.dto.PostTopicModelingOccurrencesDTO;
import company.a.charlee.entity.dto.ReactionsEngagementForPostsDTO;
import company.a.charlee.entity.telegram.TelegramPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelegramPostRepository extends JpaRepository<TelegramPost, String> {
    List<TelegramPost> findByIsProcessedFalse();
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
            "SELECT CAST(t1.post_date AS date) AS postDate, AVG(t1.sentiment_value) AS sentimentValue\n" +
            "FROM telegram_post t1 JOIN telegram_channel t2 ON t1.channel_id = t2.channel_id\n" +
            "WHERE t2.channel_title = :telegramChannelTitle\n" +
            "GROUP BY CAST(t1.post_date AS date)\n" +
            "ORDER BY CAST(t1.post_date AS date);\n")
    List<AveragePostsSentimentValueDTO> getAverageSentimentValuesForChannelPostsByDates(
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
    List<PostTopicModelingOccurrencesDTO> getPostTopicModelingOccurrencesByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle,
            @Param("topicsLimitByDate") Integer topicsLimitByDate
    );

    @Query(nativeQuery = true, value =
            "SELECT CAST(t2.post_date AS date) AS postDate, COUNT(t1.comment_id) AS totalComments\n" +
            "FROM telegram_comment t1 JOIN telegram_post t2 ON t1.post_id = t2.id JOIN telegram_channel t3 ON t2.channel_id = t3.channel_id \n" +
            "WHERE t3.channel_title = :telegramChannelTitle \n" +
            "GROUP BY CAST(t2.post_date AS date)\n" +
            "ORDER BY CAST(t2.post_date AS date);\n")
    List<CommentsEngagementForPostsDTO> getCommentsEngagementForPostsByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle
    );

    @Query(nativeQuery = true, value =
            "SELECT CAST(t2.post_date AS date) AS postDate, SUM(t1.count) AS totalReactions\n" +
            "FROM telegram_reaction t1 JOIN telegram_post t2 ON t1.post_id = t2.id JOIN telegram_channel t3 ON t2.channel_id = t3.channel_id \n" +
            "WHERE t3.channel_title = :telegramChannelTitle \n" +
            "GROUP BY CAST(t2.post_date AS date)\n" +
            "ORDER BY CAST(t2.post_date AS date);\n")
    List<ReactionsEngagementForPostsDTO> getReactionsEngagementForPostsByDates(
            @Param("telegramChannelTitle") String telegramChannelTitle
    );

}