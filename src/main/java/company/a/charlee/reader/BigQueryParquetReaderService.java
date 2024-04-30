package company.a.charlee.reader;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import company.a.charlee.entity.processed.ProcessedFile;
import company.a.charlee.repository.ProcessedFileRepository;
import company.a.charlee.services.SocialMediaParquetProcessor;
import company.a.charlee.services.telegram.TelegramProcessingService;
import company.a.charlee.services.youtube.YoutubeProcessingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class BigQueryParquetReaderService {

    private final SocialMediaParquetProcessor telegramProcessor;
    private final SocialMediaParquetProcessor youtubeProcessor;
    private final BigQuery bigQueryForTelegram;
    private final BigQuery bigQueryForYoutube;
    private final ProcessedFileRepository processedFileRepository;

    public BigQueryParquetReaderService(
            TelegramProcessingService telegramProcessor,
            YoutubeProcessingService youtubeProcessor,
            @Qualifier("bigQueryForTelegram") BigQuery bigQueryForTelegram,
            @Qualifier("bigQueryForYoutube") BigQuery bigQueryForYoutube, ProcessedFileRepository processedFileRepository) {
        this.telegramProcessor = telegramProcessor;
        this.youtubeProcessor = youtubeProcessor;
        this.bigQueryForTelegram = bigQueryForTelegram;
        this.bigQueryForYoutube = bigQueryForYoutube;
        this.processedFileRepository = processedFileRepository;
    }

    private void checkAndProcessNewFiles(String mediaType) throws InterruptedException {
        if (mediaType.equals("telegram")) {
            String queryString = "SELECT * FROM `team-bravo-telegram-export.telegram.posts` WHERE post_date BETWEEN '2024-04-15' AND '2024-04-30'";
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(queryString).setUseLegacySql(false).build();
            TableResult result = bigQueryForTelegram.query(queryConfig);

            result.iterateAll().forEach(row -> {
                String fileName = row.get("id").getStringValue();
                Optional<ProcessedFile> existingFile = processedFileRepository.findByBigQueryId(fileName);
                if (!existingFile.isPresent() || Boolean.FALSE.equals(existingFile.get().getIsProcessed())) {
                    telegramProcessor.processBigQueryResult(result);
                }
            });
        }
        else {
//            processQueryForYouTube("SELECT * FROM `youtube-fetcher-418222.youtube.channel`");
//            processQueryForYouTube("SELECT * FROM `youtube-fetcher-418222.youtube.video`");
//            processQueryForYouTube("SELECT * FROM `youtube-fetcher-418222.youtube.comment`");
//            processQueryForYouTube("SELECT * FROM `youtube-fetcher-418222.youtube.subtitle`");

        }
    }
    private void processQueryForYouTube(String queryString) throws InterruptedException {
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(queryString).setUseLegacySql(false).build();
        TableResult result = bigQueryForYoutube.query(queryConfig);
        processResults(result);
    }

    private void processResults(TableResult result) throws InterruptedException {
        result.iterateAll().forEach(row -> {
            String fileName = row.get("id").getStringValue();
            Optional<ProcessedFile> existingFile = processedFileRepository.findByBigQueryId(fileName);
            if (!existingFile.isPresent() || Boolean.FALSE.equals(existingFile.get().getIsProcessed())) {
                youtubeProcessor.processBigQueryResult(result);
            }
        });
    }

//    @Scheduled(fixedDelay = 3600000)
//    public void scheduledCheckForTelegramFiles() throws InterruptedException {
//        checkAndProcessNewFiles("telegram");
//    }

    @Scheduled(fixedDelay = 3600000)
    public void scheduledCheckForYoutubeFiles() throws InterruptedException {
        checkAndProcessNewFiles("youtube");
    }
}