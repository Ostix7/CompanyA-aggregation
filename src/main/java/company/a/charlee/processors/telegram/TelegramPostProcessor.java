package company.a.charlee.processors.telegram;

import company.a.charlee.entity.telegram.TelegramPost;
import company.a.charlee.repository.telegram.TelegramPostRepository;
import company.a.charlee.services.telegram.TelegramProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TelegramPostProcessor {
    private final TelegramPostRepository telegramPostRepository;
    private final TelegramProcessingService telegramProcessingService;

    public TelegramPostProcessor(TelegramPostRepository telegramPostRepository, TelegramProcessingService telegramProcessingService) {
        this.telegramPostRepository = telegramPostRepository;
        this.telegramProcessingService = telegramProcessingService;
    }

//    @Scheduled(fixedRate = 600000, initialDelay = 0)
//    public void processUnprocessedPosts() {
//        log.info("STARTED PROCESSING TELEGRAM POSTS ");
//        List<TelegramPost> unprocessedPosts;
//        do {
//            unprocessedPosts = telegramPostRepository.get100NotProcessedPosts();
//            for (TelegramPost post : unprocessedPosts) {
//                try {
//                    telegramProcessingService.doAnalyse(post);
//                    post.setProcessed(true);
//                    telegramPostRepository.save(post);
//                } catch (Exception e) {
//                    log.error("FAILED TO PROCESS TELEGRAM POST: " + e.getMessage());
//                }
//            }
//        } while (unprocessedPosts.size() == 100);
//    }

}