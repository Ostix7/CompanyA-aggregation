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
import company.a.charlee.services.topicmodeling.TopicModelingService;
import company.a.charlee.utils.DetectedLanguage;
import company.a.charlee.utils.MultiLanguageDetector;
import company.a.charlee.utils.MultiLanguageTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class YoutubeProcessingService implements SocialMediaParquetProcessor {

    private final YoutubeChannelService youtubeChannelService;
    private final YoutubeVideoService youtubeVideoService;
    private final YoutubeCommentService youtubeCommentService;
    private final YoutubeCaptionService youtubeCaptionService;
    private final ProcessedFileRepository processedFileRepository;
    private final SentimentAnalyzer analyzer;
    private final TopicModelingService topicModelingService;
    private final MultiLanguageDetector languageDetector;
    private final MultiLanguageTokenizer tokenizer;


    @Override
    public void processBigQueryResult(TableResult tableResult) {
        tableResult.iterateAll().forEach(row -> {
            try {
                processVideos(row);
            } catch (RuntimeException ignore) {}

            try {
                processChannels(row);
            } catch (RuntimeException ignore) {}

            try {
                processComments(row);
            } catch (RuntimeException ignore) {}

            try {
                processCaptions(row);
            } catch (RuntimeException ignore) {}
        });
    }


    private void processVideos(FieldValueList videoRow) {
        Optional<ProcessedFile> existingProcessedFile = processedFileRepository.findByBigQueryId(videoRow.get("id").getStringValue());
        if (existingProcessedFile.isPresent() && Boolean.TRUE.equals(existingProcessedFile.get().getIsProcessed())) {
            return;
        }

        YoutubeVideo video = new YoutubeVideo();
        video.setId(videoRow.get("id").getStringValue());
        video.setYoutubeVideoId(videoRow.get("youtube_video_id").getStringValue());
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
            long publishedAtMillis = videoRow.get("published_at").getTimestampValue() / 1000000; // Convert microseconds to milliseconds
            video.setPublishedAt((publishedAtMillis));
        }
        if (!videoRow.get("youtube_channel_id").isNull()) {
            youtubeChannelService.findLatestByChannelId(videoRow.get("youtube_channel_id").getStringValue())
                    .ifPresent(video::setYoutubeChannel);
        }
        if (!videoRow.get("insertion_time").isNull()) {
            long insertionTimeMillis = videoRow.get("insertion_time").getTimestampValue() / 1000000; // Convert microseconds to milliseconds
            video.setInsertionTime(insertionTimeMillis);
        } else {
            video.setInsertionTime(new Date().getTime());
        }
        video.setFetchedAt(new Date());
        youtubeVideoService.save(video);
        ProcessedFile processedFile = new ProcessedFile();
        processedFile.setBigQueryId(videoRow.get("id").getStringValue());
        processedFile.setIsProcessed(true);
        processedFile.setMediaType("youtube");
        processedFileRepository.save(processedFile);
    }
    private void processChannels(FieldValueList channelRow) {
        Optional<ProcessedFile> existingProcessedFile = processedFileRepository.findByBigQueryId(channelRow.get("id").getStringValue());
        if (existingProcessedFile.isPresent() && existingProcessedFile.get().getIsProcessed()) {
            return;
        }
        YoutubeChannel channel = new YoutubeChannel();
        channel.setId(channelRow.get("id").getStringValue());
        channel.setYoutubeChannelId(channelRow.get("youtube_channel_id").getStringValue());
        if (!channelRow.get("title").isNull()) {
            channel.setTitle(channelRow.get("title").getStringValue());
        }
        if (!channelRow.get("subscribers_count").isNull()) {
            channel.setSubscribersCount(channelRow.get("subscribers_count").getLongValue());
        }
        if (!channelRow.get("insertion_time").isNull()) {
            long insertionTimeMillis = channelRow.get("insertion_time").getTimestampValue() / 1000000;
            channel.setInsertionTime((insertionTimeMillis));
        } else {
            channel.setInsertionTime(new Date().getTime());
        }
        youtubeChannelService.save(channel);
        ProcessedFile processedFile = new ProcessedFile();
        processedFile.setBigQueryId(channelRow.get("id").getStringValue());
        processedFile.setIsProcessed(true);
        processedFile.setMediaType("youtube");
        processedFileRepository.save(processedFile);
    }
    private void processComments(FieldValueList commentRow) {
        Optional<ProcessedFile> existingProcessedFile = processedFileRepository.findByBigQueryId(commentRow.get("id").getStringValue());
        if (existingProcessedFile.isPresent() && existingProcessedFile.get().getIsProcessed()) {
            return; // Skip processing if already processed
        }
        YoutubeComment comment = new YoutubeComment();
        comment.setId(commentRow.get("id").getStringValue());
        comment.setYoutubeCommentId(commentRow.get("youtube_comment_id").getStringValue());
        YoutubeVideo youtubeVideo = youtubeVideoService.findLatestByVideoId(commentRow.get("youtube_video_id").getStringValue())
                .orElse(null);
        comment.setYoutubeVideo(youtubeVideo);
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
            long publishedAtMillis = commentRow.get("published_at").getTimestampValue() / 1000;
            comment.setPublishedAt((publishedAtMillis));
        }
        if (!commentRow.get("insertion_time").isNull()) {
            long insertionTimeMillis = commentRow.get("insertion_time").getTimestampValue() / 1000;
            comment.setInsertionTime(insertionTimeMillis);
        } else {
            comment.setInsertionTime(new Date().getTime());
        }
        youtubeCommentService.save(comment);
        ProcessedFile processedFile = new ProcessedFile();
        processedFile.setBigQueryId(commentRow.get("id").getStringValue());
        processedFile.setIsProcessed(true);
        processedFile.setMediaType("youtube");
        processedFileRepository.save(processedFile);
    }

    private void processCaptions(FieldValueList captionRow) {
        Optional<ProcessedFile> existingProcessedFile = processedFileRepository.findByBigQueryId(captionRow.get("id").getStringValue());
        if (existingProcessedFile.isPresent() && existingProcessedFile.get().getIsProcessed()) {
            return; // Skip processing if already processed
        }
        YoutubeCaption caption = new YoutubeCaption();
        caption.setId(captionRow.get("id").getStringValue());
        YoutubeVideo youtubeVideo = youtubeVideoService.findLatestByVideoId(captionRow.get("youtube_video_id").getStringValue())
                .orElse(null);
        caption.setYoutubeVideo(youtubeVideo);
        if (!captionRow.get("language").isNull()) {
            caption.setLanguage(captionRow.get("language").getStringValue());
        }
        if (!captionRow.get("content").isNull()) {
            caption.setContent(captionRow.get("content").getStringValue());
        }
        if (!captionRow.get("insertion_time").isNull()) {
            long insertionTimeMillis = captionRow.get("insertion_time").getTimestampValue() / 1000000; // Convert microseconds to milliseconds
            caption.setInsertionTime((insertionTimeMillis));
        } else {
            caption.setInsertionTime(new Date().getTime());
        }
        youtubeCaptionService.save(caption);
        ProcessedFile processedFile = new ProcessedFile();
        processedFile.setBigQueryId(caption.getId());
        processedFile.setIsProcessed(true);
        processedFile.setMediaType("youtube");
        processedFileRepository.save(processedFile);
    }


    public void doAnalyse(YoutubeVideo youtubeVideo) {
        List<YoutubeCaption> captions = youtubeVideo.getCaptions();
        for(YoutubeCaption caption : captions) {
            DetectedLanguage lang = languageDetector.detectLanguage(caption.getContent(), caption.getLanguage());
            List<String> captTokens = tokenizer.tokenize(caption.getContent(), lang);
            performTopicModelingForYoutubeCaption(caption, captTokens, lang);
            analyzer.analyseEntity(caption, captTokens, lang);
        }
    }

    private void performTopicModelingForYoutubeCaption(YoutubeCaption youtubeCaption, List<String> tokens, DetectedLanguage language) {
        List<String> topics = topicModelingService.findTopics(tokens, language);
        youtubeCaption.setTopics(topics);
    }

}
