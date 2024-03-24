package company.a.charlee.services.telegram;

import company.a.charlee.entity.telegram.*;
import company.a.charlee.services.SocialMediaParquetProcessor;
import company.a.charlee.services.sentimentAnalysis.SentimentAnalyzer;
import company.a.charlee.utils.DetectedLanguage;
import company.a.charlee.utils.MultiLanguageDetector;
import company.a.charlee.utils.MultiLanguagePOSFilter;
import company.a.charlee.utils.MultiLanguageTokenizer;
import lombok.RequiredArgsConstructor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelegramProcessingService implements SocialMediaParquetProcessor {

    private final TelegramChannelService channelService;
    private final TelegramPostService postService;
    private final SparkSession sparkSession;
    private final TelegramMediaService mediaService;

    private final TelegramCommentService commentService;

    private final TelegramReactionService reactionService;
    private final MultiLanguageTokenizer tokenizer;
    private final MultiLanguagePOSFilter posFilter;
    private final MultiLanguageDetector languageDetector;

    private final SentimentAnalyzer analyzer;

    @Override
    public void processParquet(String pathToParquetFile) {
        Dataset<Row> dataset = sparkSession.read().parquet(pathToParquetFile);
        dataset.foreach(row -> {
            TelegramChannel channel = new TelegramChannel();
            channel.setChannelId(row.getAs("channel_id"));
            channel.setChannelTitle(row.getAs("channel_title"));

            TelegramPost post = new TelegramPost();
            post.setId(row.getAs("id"));
            post.setSchemaVersion(row.getAs("schema_version"));
            post.setTelegramPostId(row.getAs("telegram_post_id"));
            post.setPostDate(row.getAs("post_date"));
            post.setPostTs(row.getAs("post_ts"));
            post.setUpdatedAt(row.getAs("updated_at"));
            post.setLang(row.getAs("lang"));
            post.setSegment(row.getAs("segment"));
            post.setFullText(row.getAs("full_text"));
            post.setViewCount(row.getAs("view_count"));
            post.setProcessed(false);

            List<Row> mediaList = row.getList(row.fieldIndex("media"));
            if (mediaList != null) {
                for (Row mediaRow : mediaList) {
                    TelegramMedia media = new TelegramMedia();
                    media.setMediaId(mediaRow.getAs("media_id"));
                    media.setMediaType(mediaRow.getAs("media_type"));
                    media.setMediaDuration(mediaRow.getAs("media_duration"));
                    media = mediaService.save(media);
                    post.addMedia(media);
                }
            }

            List<Row> commentList = row.getList(row.fieldIndex("comments"));
            if (commentList != null) {
                for (Row commentRow : commentList) {
                    TelegramComment comment = new TelegramComment();
                    comment.setTelegramMessageId(commentRow.getAs("telegram_message_id"));
                    comment.setSenderId(commentRow.getAs("sender_id"));
                    comment.setSenderUsername(commentRow.getAs("sender_username"));
                    comment.setFullText(commentRow.getAs("full_text"));
                    comment = commentService.save(comment);
                    post.addComment(comment);
                }
            }

            List<Row> reactionList = row.getList(row.fieldIndex("reactions"));
            if (reactionList != null) {
                for (Row reactionRow : reactionList) {
                    TelegramReaction reaction = new TelegramReaction();
                    reaction.setEmoji(reactionRow.getAs("emoji"));
                    reaction.setCount(reactionRow.getAs("count"));
                    reaction = reactionService.save(reaction);
                    post.addReaction(reaction);
                }
            }

            channelService.save(channel);
            postService.save(post);
        });
    }
    public void doAnalyse(TelegramPost telegramPost) {
        //TODO: starting impl of algorithms
        String postText = telegramPost.getFullText();
        DetectedLanguage language = languageDetector.detectLanguage(postText);
        List<String> tokens = tokenizer.tokenize(postText, language);
        performTopicModeling(telegramPost, tokens, language);
        analyzer.analysePost(telegramPost, tokens, language);
    }

    private void performTopicModeling(TelegramPost telegramPost, List<String> tokens, DetectedLanguage language) {
        List<String> filteredTokens = posFilter.filterSignificantPOS(tokens, language);
        Map<String, Integer> significantWordOccurrences = new HashMap<>();
        for (String filteredToken : filteredTokens) {
            significantWordOccurrences.put(filteredToken, significantWordOccurrences.getOrDefault(filteredToken, 0) + 1);
        }
        List<String> topFiveWords = significantWordOccurrences.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        telegramPost.setTopics(topFiveWords);
    }

}