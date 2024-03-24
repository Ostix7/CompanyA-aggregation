package company.a.charlee.reader;

import company.a.charlee.services.SocialMediaParquetProcessor;
import company.a.charlee.services.telegram.TelegramProcessingService;
import company.a.charlee.services.youtube.YoutubeProcessingService;
import org.apache.spark.sql.Dataset;
import org.springframework.stereotype.Service;


@Service
public class ParquetReaderService {

    private final SocialMediaParquetProcessor telegramProcessor;
    private final SocialMediaParquetProcessor youtubeProcessor;

    public ParquetReaderService(TelegramProcessingService telegramProcessor, YoutubeProcessingService youtubeProcessor) {
        this.telegramProcessor = telegramProcessor;
        this.youtubeProcessor = youtubeProcessor;
    }

    public void readAndProcessParquetFiles(String pathToParquetFile, String mediaType) {
        if ("telegram".equals(mediaType)) {
            telegramProcessor.processParquet(pathToParquetFile);
        } else if ("youtube".equals(mediaType)) {
            youtubeProcessor.processParquet(pathToParquetFile);
        } else {
            throw new IllegalArgumentException("Unsupported media type: " + mediaType);
        }
    }
}