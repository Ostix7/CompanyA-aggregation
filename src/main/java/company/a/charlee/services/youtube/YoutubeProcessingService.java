package company.a.charlee.services.youtube;

import company.a.charlee.entity.telegram.*;
import company.a.charlee.entity.youtube.YoutubeCaption;
import company.a.charlee.entity.youtube.YoutubeChannel;
import company.a.charlee.entity.youtube.YoutubeComment;
import company.a.charlee.entity.youtube.YoutubeVideo;
import company.a.charlee.services.SocialMediaParquetProcessor;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class YoutubeProcessingService implements SocialMediaParquetProcessor {

    private final SparkSession sparkSession;
    private final YoutubeChannelService youtubeChannelService;
    private final YoutubeVideoService youtubeVideoService;
    private final YoutubeCommentService youtubeCommentService;
    private final YoutubeCaptionService youtubeCaptionService;


    public YoutubeProcessingService(SparkSession sparkSession, YoutubeChannelService youtubeChannelService, YoutubeVideoService youtubeVideoService, YoutubeCommentService youtubeCommentService, YoutubeCaptionService youtubeCaptionService) {
        this.sparkSession = sparkSession;
        this.youtubeChannelService = youtubeChannelService;
        this.youtubeVideoService = youtubeVideoService;
        this.youtubeCommentService = youtubeCommentService;
        this.youtubeCaptionService = youtubeCaptionService;
    }

    @Override
    public void processParquet(String pathToParquetFile) {
        Dataset<Row> dataset = sparkSession.read().parquet(pathToParquetFile);
        dataset.foreach(row -> {
            YoutubeChannel channel = new YoutubeChannel();
            channel.setId(row.getAs("channel_id"));
            channel.setYoutubeChannelId(row.getAs("youtube_channel_id"));
            channel.setTitle(row.getAs("title"));
            channel.setSubscribersCount(row.getAs("subscribers_count"));
            List<String> videoIds = row.getList(row.fieldIndex("video_ids"));
            channel.setVideoIds(videoIds);
            channel.setInsertionTime(row.getAs("insertion_time"));
            channel = youtubeChannelService.save(channel);

            YoutubeVideo video = new YoutubeVideo();
            video.setId(row.getAs("id"));
            video.setYoutubeVideoId(row.getAs("youtube_video_id"));
            video.setTitle(row.getAs("title"));
            video.setDescription(row.getAs("description"));
            video.setLikes(row.getAs("likes"));
            List<String> tags = row.getList(row.fieldIndex("tags"));
            video.setTags(tags);
            video.setPublishedAt(row.getAs("published_at"));
            video.setInsertionTime(row.getAs("insertion_time"));
            video.setYoutubeChannel(channel);
            video.setIsProcessed(false);
            video = youtubeVideoService.save(video);

            List<Row> commentList = row.getList(row.fieldIndex("comments"));
            if (commentList != null) {
                for (Row commentRow : commentList) {
                    YoutubeComment comment = new YoutubeComment();
                    comment.setId(commentRow.getAs("id"));
                    comment.setYoutubeCommentId(commentRow.getAs("youtube_comment_id"));
                    comment.setYoutubeVideo(video);
                    comment.setText(commentRow.getAs("text"));
                    comment.setLikes(commentRow.getAs("likes"));
                    comment.setAuthorName(commentRow.getAs("author_name"));
                    comment.setAuthorProfileImageUrl(commentRow.getAs("author_profile_image_url"));
                    comment.setPublishedAt(commentRow.getAs("published_at"));
                    comment.setInsertionTime(commentRow.getAs("insertion_time"));
                    youtubeCommentService.save(comment);
                }
            }
            List<Row> captionList = row.getList(row.fieldIndex("captions"));
            if (captionList != null) {
                for (Row captionRow : captionList) {
                    YoutubeCaption caption = new YoutubeCaption();
                    caption.setId(captionRow.getAs("id"));
                    caption.setYoutubeVideo(video);
                    caption.setLanguage(captionRow.getAs("language"));
                    caption.setContent(captionRow.getAs("content"));
                    caption.setInsertionTime(captionRow.getAs("insertion_time"));
                    youtubeCaptionService.save(caption);
                }
            }

        });
    }
    public void doAnalyse(YoutubeVideo youtubeVideo) {
        //TODO: starting impl of algorithms
    }
}
