package company.a.charlee.services.youtube;

import company.a.charlee.entity.dto.*;
import company.a.charlee.entity.youtube.YoutubeVideo;
import company.a.charlee.repository.youtube.YoutubeChannelRepository;
import company.a.charlee.repository.youtube.YoutubeVideoRepository;
import company.a.charlee.repository.youtube.YoutubeCommentRepository;
import company.a.charlee.repository.youtube.YoutubeCaptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

@Service
public class YoutubeService {

    private final YoutubeVideoRepository videoRepository;
    private final YoutubeChannelRepository channelRepository;
    private final YoutubeCommentRepository commentRepository;
    private final YoutubeCaptionRepository captionRepository;

    @Autowired
    public YoutubeService(
            YoutubeVideoRepository videoRepository,
            YoutubeChannelRepository channelRepository,
            YoutubeCommentRepository commentRepository,
            YoutubeCaptionRepository captionRepository
    ) {
        this.videoRepository = videoRepository;
        this.channelRepository = channelRepository;
        this.commentRepository = commentRepository;
        this.captionRepository = captionRepository;
    }

    public List<VideoInfoDTO> findMostLikedVideosInDateRange(String channelId, String channelName, long startTimestamp, long endTimestamp, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<YoutubeVideo> videos = videoRepository.findMostLikedByDateRangeAndChannel(channelId, channelName, startTimestamp, endTimestamp, pageable);

        // Removing duplicates based on 'youtube_video_id'
        List<VideoInfoDTO> uniqueVideos = videos.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                YoutubeVideo::getYoutubeVideoId, // keyMapper
                                video -> new VideoInfoDTO(
                                        video.getTitle(),
                                        video.getLikes(),
                                        video.getYoutubeChannel().getTitle(),
                                        new Date(video.getPublishedAt())),
                                (existing, replacement) -> existing, // if the same key is encountered, keep the existing
                                LinkedHashMap::new),
                        map -> new ArrayList<>(map.values())));

        return uniqueVideos;
    }

    public List<ActiveCommenterDTO> findActiveCommenters(String channelId, String channelName, long startTimestamp, long endTimestamp, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return commentRepository.findActiveCommentersByChannel(channelId, channelName, startTimestamp, endTimestamp, pageable);
    }

    public List<YoutubeChannelDTO> listAllYouTubeChannelNames() {
        return channelRepository.findAllChannelNames();
    }

    public YoutubeAverageSentimentDTO calculateAverageSentiment(Long startTime, Long endTime) {
        Double averageSentiment = captionRepository.findAverageSentimentBetweenTimestamps(startTime, endTime);
        if (averageSentiment == null) {
            averageSentiment = 0.0; // Default to 0 if no data is found
        }
        return new YoutubeAverageSentimentDTO(averageSentiment);
    }

    public List<TopicInfoDTO> findMostPopularTopicsInDateRange(String channelId, String channelName, String language, long startTimestamp, long endTimestamp, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return captionRepository.findMostPopularTopicsInDateRange(channelId, channelName, language, startTimestamp, endTimestamp, pageable);
    }

    public List<TopicPhraseInfoDTO> findMostPopularTopicPhrasesInDateRange(String channelId, String channelName, String language, long startTimestamp, long endTimestamp, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return captionRepository.findMostPopularTopicPhrasesInDateRange(channelId, channelName, language, startTimestamp, endTimestamp, pageable);
    }

    public List<YoutubeCommentDTO> findMostLikedCommentsForVideo(String videoId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return commentRepository.findMostLikedCommentsForVideo(videoId, pageable);
    }

    public List<YoutubePopularCommentDTO> findMostLikedCommentsInDateRange(String channelId, String channelName, long startTimestamp, long endTimestamp, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return commentRepository.findMostLikedCommentsInDateRange(channelId, channelName, startTimestamp, endTimestamp, pageable);
    }
}
