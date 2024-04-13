package company.a.charlee.services.youtube;

import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import company.a.charlee.entity.youtube.YoutubeCaption;
import company.a.charlee.entity.youtube.YoutubeChannel;
import company.a.charlee.entity.youtube.YoutubeComment;
import company.a.charlee.entity.youtube.YoutubeVideo;
import company.a.charlee.services.SocialMediaParquetProcessor;

import company.a.charlee.services.sentimentAnalysis.SentimentAnalyzer;
import company.a.charlee.utils.DetectedLanguage;
import company.a.charlee.utils.MultiLanguageDetector;
import company.a.charlee.utils.MultiLanguageTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeProcessingService implements SocialMediaParquetProcessor {

    private final YoutubeChannelService youtubeChannelService;
    private final YoutubeVideoService youtubeVideoService;
    private final YoutubeCommentService youtubeCommentService;
    private final YoutubeCaptionService youtubeCaptionService;
    private final SentimentAnalyzer analyzer;
    private final MultiLanguageDetector languageDetector;
    private final MultiLanguageTokenizer tokenizer;


    @Override
    public void processBigQueryResult(TableResult tableResult) {
        //TODO: create ProcessedFile with unique id not to process same bigQuery again and again
        //TODO: If there is an issue "You cannot extract without schema: watch realization in telegram and how subSchema created and applied
        tableResult.iterateAll().forEach(row -> {
            YoutubeChannel channel = new YoutubeChannel();
            channel.setYoutubeChannelId(row.get("youtube_channel_id").getStringValue());
            channel.setTitle(row.get("channel_title").getStringValue());
            channel.setSubscribersCount(row.get("subscribers_count").getLongValue());
            String videoIdsJson = row.get("video_ids").getStringValue();
            List<String> videoIds = new ArrayList<>();
            channel.setVideoIds(videoIds);
            youtubeChannelService.save(channel);

            YoutubeVideo video = new YoutubeVideo();
            video.setYoutubeVideoId(row.get("youtube_video_id").getStringValue());
            video.setTitle(row.get("video_title").getStringValue());
            video.setDescription(row.get("description").getStringValue());
            video.setLikes(row.get("likes").getLongValue());
            List<String> tags = new ArrayList<>();
            video.setTags(tags);
            video.setYoutubeChannel(channel);
            youtubeVideoService.save(video);

            List<FieldValue> commentValues = row.get("comments").getRepeatedValue();
            commentValues.forEach(commentValue -> {
                FieldValueList commentRow = commentValue.getRecordValue();
                YoutubeComment comment = new YoutubeComment();
                comment.setYoutubeCommentId(commentRow.get("youtube_comment_id").getStringValue());
                comment.setText(commentRow.get("text").getStringValue());
                comment.setLikes(commentRow.get("likes").getLongValue());
                comment.setAuthorName(commentRow.get("author_name").getStringValue());
                comment.setYoutubeVideo(video);
                youtubeCommentService.save(comment);
            });

            List<FieldValue> captionValues = row.get("captions").getRepeatedValue();
            captionValues.forEach(captionValue -> {
                FieldValueList captionRow = captionValue.getRecordValue();
                YoutubeCaption caption = new YoutubeCaption();
                caption.setLanguage(captionRow.get("language").getStringValue());
                caption.setContent(captionRow.get("content").getStringValue());
                caption.setYoutubeVideo(video);
                youtubeCaptionService.save(caption);
            });
        });
    }


    public void doAnalyse(YoutubeVideo youtubeVideo) {
        //TODO: starting impl of algorithms
        String videoDescription = youtubeVideo.getDescription();
        DetectedLanguage language = languageDetector.detectLanguage(videoDescription);
        List<String> tokens = tokenizer.tokenize(videoDescription, language);
        analyzer.analyseEntity(youtubeVideo, tokens, language);

        List<YoutubeCaption> captions = youtubeVideo.getCaptions();

        for(YoutubeCaption caption : captions) {
            DetectedLanguage lang = DetectedLanguage.getFromString(caption.getLanguage());
            List<String> captTokens = tokenizer.tokenize(caption.getContent(), lang);
            analyzer.analyseEntity(caption, captTokens, lang);
        }

    }

}
