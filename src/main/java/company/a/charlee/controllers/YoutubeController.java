package company.a.charlee.controllers;

import company.a.charlee.entity.dto.*;
import company.a.charlee.services.youtube.YoutubeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/youtube/statistics")
public class YoutubeController {

    @Autowired
    private YoutubeService youtubeService;

    @GetMapping("/all-channels")
    public List<YoutubeChannelDTO> getAllYouTubeChannels() {
        return youtubeService.getAllYouTubeChannels();
    }

    @GetMapping("/channel-videos/{channelId}")
    public List<YoutubeVideoDTO> getVideosByChannel(@PathVariable("channelId") String channelId) {
        return youtubeService.getVideosByChannel(channelId);
    }

    @GetMapping("/video-comments/{videoId}")
    public List<YoutubeCommentDTO> getCommentsByVideo(@PathVariable("videoId") String videoId) {
        return youtubeService.getCommentsByVideo(videoId);
    }

    @GetMapping("/video-captions/{videoId}")
    public List<YoutubeCaptionDTO> getCaptionsByVideo(@PathVariable("videoId") String videoId) {
        return youtubeService.getCaptionsByVideo(videoId);
    }

    @GetMapping("/channels/top-by-subscribers")
    public ResponseEntity<List<YoutubeChannelDTO>> getTopChannelsBySubscribersCount(
            @RequestParam(defaultValue = "10") int limit) {
        List<YoutubeChannelDTO> topChannels = youtubeService.findTopChannelsBySubscribersCount(limit);
        return ResponseEntity.ok(topChannels);
    }

    @GetMapping("/most-liked-videos")
    public List<YoutubeVideoDTO> getMostLikedVideos() {
        return youtubeService.getMostLikedVideos();
    }

    @GetMapping("/videos/count/processed")
    public ResponseEntity<Long> getProcessedVideosCount() {
        Long count = youtubeService.countProcessedVideos();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/topics/most-popular")
    public ResponseEntity<List<TopicCountDTO>> getMostPopularTopics() {
        List<TopicCountDTO> topics = youtubeService.findMostPopularTopicsByVideoCount();
        return ResponseEntity.ok(topics);
    }
}
