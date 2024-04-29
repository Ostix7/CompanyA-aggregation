package company.a.charlee.services.youtube;

import company.a.charlee.entity.dto.*;
import company.a.charlee.entity.youtube.YoutubeChannel;
import company.a.charlee.entity.youtube.YoutubeVideo;
import company.a.charlee.entity.youtube.YoutubeComment;
import company.a.charlee.entity.youtube.YoutubeCaption;
import company.a.charlee.repository.youtube.YoutubeChannelRepository;
import company.a.charlee.repository.youtube.YoutubeVideoRepository;
import company.a.charlee.repository.youtube.YoutubeCommentRepository;
import company.a.charlee.repository.youtube.YoutubeCaptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;


import java.util.List;
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

    @Transactional(readOnly = true)
    public List<YoutubeChannelDTO> getAllYouTubeChannels() {
        return channelRepository.findAll().stream()
                .map(this::convertToChannelDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<YoutubeVideoDTO> getVideosByChannel(String channelId) {
        return videoRepository.findByChannelId(channelId)
                .orElseThrow(() -> new EntityNotFoundException("Channel not found with ID: " + channelId))
                .stream()
                .map(this::convertToVideoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<YoutubeCommentDTO> getCommentsByVideo(String videoId) {
        return commentRepository.findByVideoId(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video not found with ID: " + videoId))
                .stream()
                .map(this::convertToCommentDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<YoutubeCaptionDTO> getCaptionsByVideo(String videoId) {
        return captionRepository.findByVideoId(videoId)
                .orElseThrow(() -> new EntityNotFoundException("Video not found with ID: " + videoId))
                .stream()
                .map(this::convertToCaptionDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<YoutubeChannelDTO> findTopChannelsBySubscribersCount(int limit) {
        return channelRepository.findTopChannelsBySubscribersCount(PageRequest.of(0, limit)).stream()
                .map(this::convertToChannelDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<YoutubeVideoDTO> getMostLikedVideos() {
        int limit = 10; // Define the number of top videos to fetch
        return videoRepository.findMostLikedVideos(PageRequest.of(0, limit)).stream()
                .map(this::convertToVideoDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long countProcessedVideos() {
        return videoRepository.countByIsProcessedTrue();
    }

    @Transactional(readOnly = true)
    public List<TopicCountDTO> findMostPopularTopicsByVideoCount() {
        return captionRepository.findMostPopularTopicsByVideoCount().stream()
                .map(result -> new TopicCountDTO((String) result[0], ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }

    // Helper methods to convert entities to DTOs
    private YoutubeChannelDTO convertToChannelDTO(YoutubeChannel channel) {
        return new YoutubeChannelDTO(channel.getId(), channel.getTitle(), channel.getSubscribersCount());
    }

    private YoutubeVideoDTO convertToVideoDTO(YoutubeVideo video) {
        return new YoutubeVideoDTO(video.getId(), video.getTitle(), video.getDescription(), video.getPublishedAt());
    }

    private YoutubeCommentDTO convertToCommentDTO(YoutubeComment comment) {
        return new YoutubeCommentDTO(comment.getId(), comment.getText(), comment.getLikes(), comment.getAuthorName());
    }

    private YoutubeCaptionDTO convertToCaptionDTO(YoutubeCaption caption) {
        return new YoutubeCaptionDTO(caption.getId(), caption.getLanguage(), caption.getContent());
    }
}
