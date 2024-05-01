package company.a.charlee.processors.youtube;

import company.a.charlee.entity.youtube.YoutubeVideo;
import company.a.charlee.repository.youtube.YoutubeVideoRepository;
import company.a.charlee.services.youtube.YoutubeProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
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
        log.info("STARTED PROCESSING YOUTUBE VIDEOS");
        for (YoutubeVideo video : unprocessedVideos) {
            try {
                youtubeProcessingService.doAnalyse(video);
                video.setIsProcessed(true);
                youtubeVideoRepository.save(video);
            } catch (Exception e) {
                log.error("FAILED TO PROCESS YOUTUBE VIDEO: " + e.getMessage());
            }
        }
    }

}