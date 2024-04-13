package company.a.charlee.services.youtube;

import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import company.a.charlee.entity.processed.ProcessedFile;
import company.a.charlee.entity.youtube.YoutubeCaption;
import company.a.charlee.entity.youtube.YoutubeChannel;
import company.a.charlee.entity.youtube.YoutubeComment;
import company.a.charlee.entity.youtube.YoutubeVideo;
import company.a.charlee.repository.ProcessedFileRepository;
import company.a.charlee.services.SocialMediaParquetProcessor;

import company.a.charlee.services.sentimentAnalysis.SentimentAnalyzer;
import company.a.charlee.utils.DetectedLanguage;
import company.a.charlee.utils.MultiLanguageDetector;
import company.a.charlee.utils.MultiLanguageTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class YoutubeProcessingService implements SocialMediaParquetProcessor {

    private final YoutubeChannelService youtubeChannelService;
    private final YoutubeVideoService youtubeVideoService;
    private final YoutubeCommentService youtubeCommentService;
    private final YoutubeCaptionService youtubeCaptionService;
    private final ProcessedFileRepository processedFileRepository;
    private final SentimentAnalyzer analyzer;
    private final MultiLanguageDetector languageDetector;
    private final MultiLanguageTokenizer tokenizer;


    @Override
    public void processBigQueryResult(TableResult tableResult) {
        tableResult.iterateAll().forEach(row -> {
            try {
                processVideos(row);
            } catch (IllegalArgumentException ignore) {}

            try {
                processChannels(row);
            } catch (IllegalArgumentException ignore) {}

            try {
                processComments(row);
            } catch (IllegalArgumentException ignore) {}

            try {
                processCaptions(row);
            } catch (IllegalArgumentException ignore) {}
        });
    }


    private void processVideos(FieldValueList videoRow) {
        String videoId = videoRow.get("youtube_video_id").getStringValue();
        YoutubeVideo video = youtubeVideoService.findByYoutubeVideoId(videoId);
        if (video == null) {
            video = new YoutubeVideo();
            video.setId(videoRow.get("id").getStringValue());
        }
        video.setYoutubeVideoId(videoId);

        if (!videoRow.get("title").isNull()) {
            video.setTitle(videoRow.get("title").getStringValue());
        }
        if (!videoRow.get("description").isNull()) {
            video.setDescription(videoRow.get("description").getStringValue());
        }
        if (!videoRow.get("likes").isNull()) {
            video.setLikes(videoRow.get("likes").getLongValue());
        }
        if (!videoRow.get("published_at").isNull()) {
            video.setPublishedAt(videoRow.get("published_at").getTimestampValue());
        }
        video.setInsertionTime(System.currentTimeMillis());

        youtubeVideoService.save(video);
        ProcessedFile processedFile = new ProcessedFile();
        processedFile.setBigQueryId(videoRow.get("id").getStringValue());
        processedFileRepository.save(processedFile);
    }
    private void processChannels(FieldValueList channelRow) {
        String channelId = channelRow.get("youtube_channel_id").getStringValue();
        YoutubeChannel channel = youtubeChannelService.findByChannelId(channelId);
        if (channel == null) {
            channel = new YoutubeChannel();
            channel.setId(channelRow.get("id").getStringValue());
        }
        channel.setYoutubeChannelId(channelId);

        if (!channelRow.get("title").isNull()) {
            channel.setTitle(channelRow.get("title").getStringValue());
        }
        if (!channelRow.get("subscribers_count").isNull()) {
            channel.setSubscribersCount(channelRow.get("subscribers_count").getLongValue());
        }
        channel.setInsertionTime(System.currentTimeMillis());

        youtubeChannelService.save(channel);
        ProcessedFile processedFile = new ProcessedFile();
        processedFile.setBigQueryId(channelRow.get("id").getStringValue());
        processedFileRepository.save(processedFile);
    }
    private void processComments(FieldValueList commentRow) {

        YoutubeComment comment = youtubeCommentService.findByCommentId(commentRow.get("youtube_comment_id").getStringValue());
        if(comment ==null) {
             comment = new YoutubeComment();
            comment.setId(commentRow.get("id").getStringValue());
        }
        comment.setYoutubeCommentId(commentRow.get("youtube_comment_id").getStringValue());
        if (!commentRow.get("text").isNull()) {
            comment.setText(commentRow.get("text").getStringValue());
        }
        if (!commentRow.get("likes").isNull()) {
            comment.setLikes(commentRow.get("likes").getLongValue());
        }
        if (!commentRow.get("author_name").isNull()) {
            comment.setAuthorName(commentRow.get("author_name").getStringValue());
        }
        if (!commentRow.get("author_profile_image_url").isNull()) {
            comment.setAuthorProfileImageUrl(commentRow.get("author_profile_image_url").getStringValue());
        }
        if (!commentRow.get("published_at").isNull()) {
            comment.setPublishedAt(commentRow.get("published_at").getTimestampValue());
        }
        comment.setInsertionTime(System.currentTimeMillis());

        youtubeCommentService.save(comment);
        ProcessedFile processedFile = new ProcessedFile();
        processedFile.setBigQueryId(commentRow.get("id").getStringValue());
        processedFileRepository.save(processedFile);
    }

    private void processCaptions(FieldValueList captionRow) {
        YoutubeCaption caption = youtubeCaptionService.findByYoutubeVideoId(captionRow.get("youtube_video_id").getStringValue());
        if(caption ==null) {
            caption = new YoutubeCaption();
            caption.setId(captionRow.get("id").getStringValue());
        }

        if (!captionRow.get("language").isNull()) {
            caption.setLanguage(captionRow.get("language").getStringValue());
        }
        if (!captionRow.get("content").isNull()) {
            caption.setContent(captionRow.get("content").getStringValue());
        }
        caption.setInsertionTime(System.currentTimeMillis());
        caption.setYoutubeVideo(youtubeVideoService.findByYoutubeVideoId(captionRow.get("youtube_video_id").getStringValue()));
        youtubeCaptionService.save(caption);
        ProcessedFile processedFile = new ProcessedFile();
        processedFile.setBigQueryId(caption.getId());
        processedFileRepository.save(processedFile);
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
