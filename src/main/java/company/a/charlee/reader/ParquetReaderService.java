package company.a.charlee.reader;

import company.a.charlee.services.SocialMediaParquetProcessor;
import company.a.charlee.services.telegram.TelegramProcessingService;
import company.a.charlee.services.youtube.YoutubeProcessingService;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class ParquetReaderService {

    private final SparkSession sparkSession;
    private final SocialMediaParquetProcessor telegramProcessor;
    private final SocialMediaParquetProcessor youtubeProcessor;

    public ParquetReaderService(SparkSession sparkSession, TelegramProcessingService telegramProcessor, YoutubeProcessingService youtubeProcessor) {
        this.sparkSession = sparkSession;
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