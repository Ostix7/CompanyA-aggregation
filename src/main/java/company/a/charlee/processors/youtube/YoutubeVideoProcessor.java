package company.a.charlee.processors.youtube;

import company.a.charlee.entity.youtube.YoutubeVideo;
import company.a.charlee.repository.youtube.YoutubeVideoRepository;
import company.a.charlee.services.youtube.YoutubeProcessingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class YoutubeVideoProcessor {

    private final YoutubeVideoRepository youtubeVideoRepository;

    private final YoutubeProcessingService youtubeProcessingService;

    public YoutubeVideoProcessor(YoutubeVideoRepository youtubeVideoRepository, YoutubeProcessingService youtubeProcessingService) {
        this.youtubeVideoRepository = youtubeVideoRepository;
        this.youtubeProcessingService = youtubeProcessingService;
    }

    @Scheduled(fixedRate = 600000, initialDelay = 300000)
    public void processUnprocessedVideos() {
        List<YoutubeVideo> unprocessedVideos = youtubeVideoRepository.findByIsProcessedFalse();
        for (YoutubeVideo video : unprocessedVideos) {
            youtubeProcessingService.doAnalyse(video);
            video.setIsProcessed(true);
            youtubeVideoRepository.save(video);
        }
    }
}