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
//            LocalDate now = LocalDate.now();
//            LocalDate yearAgo = now.minusYears(1);
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String formattedNow = now.format(formatter);
//            String formattedYearAgo = yearAgo.format(formatter);
            String queryString = "SELECT * FROM `team-bravo-telegram-export.telegram.mocks` WHERE post_date BETWEEN '2022-04-07' AND '2024-04-07'";

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
            String queryString = "SELECT * FROM `youtube-bravo-export.youtube.mocks` WHERE post_date BETWEEN '2022-04-07' AND '2024-04-07'";
            QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(queryString).setUseLegacySql(false).build();
            TableResult result = bigQueryForTelegram.query(queryConfig);

            result.iterateAll().forEach(row -> {
                String fileName = row.get("id").getStringValue();
                Optional<ProcessedFile> existingFile = processedFileRepository.findByBigQueryId(fileName);
                if (!existingFile.isPresent() || Boolean.FALSE.equals(existingFile.get().getIsProcessed())) {
                    youtubeProcessor.processBigQueryResult(result);
                }
            });
        }
    }


    @Scheduled(fixedDelay = 3600000)
    public void scheduledCheckForTelegramFiles() throws InterruptedException {
        checkAndProcessNewFiles("telegram");
    }

    @Scheduled(fixedDelay = 3600000)
    public void scheduledCheckForYoutubeFiles() throws InterruptedException {
        checkAndProcessNewFiles("youtube");
    }
}