package company.a.charlee.services.telegram;

import com.google.cloud.bigquery.FieldList;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import company.a.charlee.entity.processed.ProcessedFile;
import company.a.charlee.entity.telegram.*;
import company.a.charlee.repository.ProcessedFileRepository;
import company.a.charlee.services.SocialMediaParquetProcessor;
import company.a.charlee.services.sentimentAnalysis.SentimentAnalyzer;
import company.a.charlee.utils.DetectedLanguage;
import company.a.charlee.utils.MultiLanguageDetector;
import company.a.charlee.utils.MultiLanguagePOSFilter;
import company.a.charlee.utils.MultiLanguageTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TelegramProcessingService implements SocialMediaParquetProcessor {

    private final TelegramChannelService channelService;
    private final TelegramPostService postService;
    private final TelegramMediaService mediaService;

    private final TelegramCommentService commentService;

    private final TelegramReactionService reactionService;
    private final MultiLanguageTokenizer tokenizer;
    private final MultiLanguagePOSFilter posFilter;
    private final MultiLanguageDetector languageDetector;

    private final SentimentAnalyzer analyzer;
    private final ProcessedFileRepository processedFileRepository;

    @Override
    public void processBigQueryResult(TableResult result) {
        ProcessedFile processedFile = new ProcessedFile();
        result.iterateAll().forEach(row -> {
            FieldList mediaSubSchema = result.getSchema().getFields().get("media").getSubFields();
            FieldList commentsSubSchema = result.getSchema().getFields().get("comments").getSubFields();
            FieldList reactionsSubSchema = result.getSchema().getFields().get("reactions").getSubFields();


            TelegramChannel channel = new TelegramChannel();
            channel.setChannelId(row.get("channel_id").getLongValue());
            channel.setChannelTitle(row.get("channel_title").getStringValue());

            TelegramPost post = new TelegramPost();
            processedFile.setBigQueryId(row.get("id").getStringValue());
            post.setId(row.get("id").getStringValue());
            post.setSchemaVersion(row.get("schema_version").getLongValue());
            post.setTelegramPostId(row.get("telegram_post_id").getLongValue());
            post.setPostDate(row.get("post_date").getStringValue());
            post.setPostTs(row.get("post_ts").getTimestampValue());
            if (row.get("updated_at") != null && !row.get("updated_at").isNull()) {
                String updatedAtStr = row.get("updated_at").getStringValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
                LocalDateTime localDateTime = LocalDateTime.parse(updatedAtStr, formatter);
                Instant updatedAt = localDateTime.toInstant(ZoneOffset.UTC);
                post.setUpdatedAt(updatedAt);
            } else {
                post.setUpdatedAt(null);
            }
            post.setLang(row.get("lang").getStringValue());
            post.setSegment(row.get("segment").getStringValue());
            post.setFullText(row.get("full_text").getStringValue());
            post.setViewCount(row.get("view_count").getLongValue());
            post.setProcessed(false);
            post = postService.save(post);

            List<FieldValue> mediaValues = row.get("media").getRepeatedValue();
            TelegramPost finalPost = post;
            mediaValues.forEach(mediaValue -> {
                FieldValueList mediaRow = mediaValue.getRecordValue();
                FieldValueList mediaRowWithSchema = FieldValueList.of(mediaRow, mediaSubSchema);
                TelegramMedia media = new TelegramMedia();
                media.setMediaId(mediaRowWithSchema.get("media_id").getStringValue());
                media.setMediaType(mediaRowWithSchema.get("media_type").getStringValue());
                if (!mediaRowWithSchema.get("media_duration").isNull()) {
                    media.setMediaDuration(mediaRowWithSchema.get("media_duration").getStringValue());
                }
                media.setPost(finalPost);
                mediaService.save(media);
            });

            List<FieldValue> commentValues = row.get("comments").getRepeatedValue();

            TelegramPost finalPost1 = post;
            commentValues.forEach(commentValue -> {
                FieldValueList commentRow = commentValue.getRecordValue();
                FieldValueList commentRowWithSchema = FieldValueList.of(commentRow, commentsSubSchema);

                TelegramComment comment = new TelegramComment();
                comment.setTelegramMessageId(commentRowWithSchema.get("telegram_message_id").getLongValue());
                comment.setSenderId(commentRowWithSchema.get("sender_id").getLongValue());
                comment.setSenderUsername(commentRowWithSchema.get("sender_username").isNull() ? "" : commentRowWithSchema.get("sender_username").getStringValue());
                comment.setSenderFirstName(commentRowWithSchema.get("sender_first_name").isNull() ? "" : commentRowWithSchema.get("sender_first_name").getStringValue());
                comment.setSenderLastName(commentRowWithSchema.get("sender_last_name").isNull() ? "" : commentRowWithSchema.get("sender_last_name").getStringValue());
                comment.setReplyTo(commentRowWithSchema.get("reply_to").isNull() ? null : commentRowWithSchema.get("reply_to").getLongValue());
                comment.setFullText(commentRowWithSchema.get("full_text").getStringValue());
                comment.setPost(finalPost1);
                comment = commentService.save(comment);

                List<FieldValue> commentReactionValues = commentRowWithSchema.get("reactions").getRepeatedValue();
                TelegramComment finalComment = comment;
                commentReactionValues.forEach(reactionValue -> {
                    FieldValueList reactionRow = FieldValueList.of(reactionValue.getRecordValue(), reactionsSubSchema);
                    TelegramReaction reaction = new TelegramReaction();
                    reaction.setEmoji(reactionRow.get("emoji").getStringValue());
                    reaction.setCount(reactionRow.get("count").getLongValue());
                    reaction.setComment(finalComment);
                    reaction = reactionService.save(reaction);
                    finalComment.addReaction(reaction);
                });

                comment = commentService.save(comment);
                finalPost1.addComment(comment);
            });

            List<FieldValue> postReactionValues = row.get("reactions").getRepeatedValue();
            TelegramPost finalPost2 = post;
            postReactionValues.forEach(reactionValue -> {
                FieldValueList reactionRowWithSchema = FieldValueList.of(reactionValue.getRecordValue(), reactionsSubSchema);

                TelegramReaction reaction = new TelegramReaction();
                reaction.setEmoji(reactionRowWithSchema.get("emoji").getStringValue());
                reaction.setCount(reactionRowWithSchema.get("count").getLongValue());
                reaction.setPost(finalPost2);
                finalPost2.addReaction(reaction);
            });

            channelService.save(channel);
            post.setChannel(channel);
            postService.save(post);
            processedFile.setIsProcessed(true);
            processedFile.setMediaType("telegram");
            processedFileRepository.save(processedFile);
        });
    }
    public void doAnalyse(TelegramPost telegramPost) {
        //TODO: starting impl of algorithms
        String postText = telegramPost.getFullText();
        DetectedLanguage language = languageDetector.detectLanguage(postText);
        List<String> tokens = tokenizer.tokenize(postText, language);
        performTopicModeling(telegramPost, tokens, language);
        analyzer.analyseEntity(telegramPost, tokens, language);
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