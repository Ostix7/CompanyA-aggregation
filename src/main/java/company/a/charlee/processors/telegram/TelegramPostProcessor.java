package company.a.charlee.processors.telegram;

import company.a.charlee.entity.telegram.TelegramPost;
import company.a.charlee.repository.telegram.TelegramPostRepository;
import company.a.charlee.services.telegram.TelegramProcessingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelegramPostProcessor {
    private final TelegramPostRepository telegramPostRepository;
    private final TelegramProcessingService telegramProcessingService;

    public TelegramPostProcessor(TelegramPostRepository telegramPostRepository, TelegramProcessingService telegramProcessingService) {
        this.telegramPostRepository = telegramPostRepository;
        this.telegramProcessingService = telegramProcessingService;
    }

    @Scheduled(fixedRate = 600000, initialDelay = 0)
    public void processUnprocessedPosts() {
        List<TelegramPost> unprocessedPosts = telegramPostRepository.findByIsProcessedFalse();
        for (TelegramPost post : unprocessedPosts) {
            try {
                telegramProcessingService.doAnalyse(post);
                post.setProcessed(true);
                telegramPostRepository.save(post);
            } catch (Exception e) {}
        }
    }

}