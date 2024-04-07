package company.a.charlee.reader;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
import company.a.charlee.entity.processed.ProcessedFile;
import company.a.charlee.repository.ProcessedFileRepository;
import company.a.charlee.services.telegram.TelegramProcessingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;


@Service
public class TelegramBigQueryParquetReaderService extends AbstractBigQueryParquetReaderService {

    public TelegramBigQueryParquetReaderService(
            TelegramProcessingService telegramProcessor,
            @Qualifier("bigQueryForTelegram") BigQuery bigQuery,
            ProcessedFileRepository processedFileRepository) {
        super(telegramProcessor, bigQuery, processedFileRepository);
    }

    @Override
    protected String getMediaType() {
        return "telegram";
    }
}