package company.a.charlee.services.telegram;

import company.a.charlee.entity.*;
import company.a.charlee.services.SocialMediaParquetProcessor;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TelegramProcessingService implements SocialMediaParquetProcessor {

    private final TelegramChannelService channelService;
    private final TelegramPostService postService;
    private final SparkSession sparkSession;
    private final TelegramMediaService mediaService;

    private final TelegramCommentService commentService;

    private final TelegramReactionService reactionService;
    private final Tokenizer tokenizer;
    private final POSTaggerME posTagger;


    @Autowired
    public TelegramProcessingService(
            TelegramChannelService channelService,
            TelegramPostService postService,
            SparkSession sparkSession,
            TelegramMediaService mediaService,
            TelegramCommentService commentService,
            TelegramReactionService reactionService,
            Tokenizer tokenizer,
            POSTaggerME posTagger
    ) {
        this.channelService = channelService;
        this.postService = postService;
        this.sparkSession = sparkSession;
        this.mediaService = mediaService;
        this.commentService = commentService;
        this.reactionService = reactionService;
        this.tokenizer = tokenizer;
        this.posTagger = posTagger;
    }

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
//            post.setChannelId(row.getAs("channel_id"));
            post.setTelegramPostId(row.getAs("telegram_post_id"));
            post.setPostDate(row.getAs("post_date"));
            post.setPostTs(row.getAs("post_ts"));
            post.setUpdatedAt(row.getAs("updated_at"));
            post.setLang(row.getAs("lang"));
            post.setSegment(row.getAs("segment"));
            post.setFullText(row.getAs("full_text"));
            post.setViewCount(row.getAs("view_count"));

            List<Row> mediaList = row.getList(row.fieldIndex("media"));
            if (mediaList != null) {
                for (Row mediaRow : mediaList) {
                    TelegramMedia media = new TelegramMedia();
                    media.setMediaId(mediaRow.getAs("media_id"));
                    media.setMediaType(mediaRow.getAs("media_type"));
                    media.setMediaDuration(mediaRow.getAs("media_duration"));
                    media = mediaService.save(media);
//                    post.addMedia(media);
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
                    // предположим, что commentService.save(comment) сохраняет и возвращает объект с ID
                    comment = commentService.save(comment);
                    // предполагается, что post имеет метод для добавления комментария
//                    post.addComment(comment);
                }
            }

            List<Row> reactionList = row.getList(row.fieldIndex("reactions"));
            if (reactionList != null) {
                for (Row reactionRow : reactionList) {
                    TelegramReaction reaction = new TelegramReaction();
                    reaction.setEmoji(reactionRow.getAs("emoji"));
                    reaction.setCount(reactionRow.getAs("count"));
                    // предположим, что reactionService.save(reaction) сохраняет и возвращает объект с ID
                    reaction = reactionService.save(reaction);
                    // предполагается, что post имеет метод для добавления реакции
//                    post.addReaction(reaction);
                }
            }


//            doAnalyse();
            channelService.save(channel);
            postService.save(post);


        });
    }
    private void doAnalyse(TelegramChannel telegramChannel, TelegramPost telegramPost, TelegramReaction reaction, TelegramComment telegramComment, TelegramMedia media) {
        //TODO: starting impl of algorithms
        performTopicModeling(telegramPost);
    }

    private void performTopicModeling(TelegramPost telegramPost) {
        String postText = telegramPost.getFullText();
        String[] tokens = tokenizer.tokenize(postText);
        String[] tags = posTagger.tag(tokens);
        Map<String, Integer> significantWordOccurrences = new HashMap<>();
        for (int i = 0; i < tags.length; i++) {
            if (tags[i].equals("NOUN") || tags[i].equals("PROPN") || tags[i].equals("NOUN+PART") || tags[i].equals("PROPN+PART")) {
                significantWordOccurrences.put(tokens[i], significantWordOccurrences.getOrDefault(tokens[i], 0) + 1);
            }
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