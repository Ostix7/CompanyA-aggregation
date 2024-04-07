package company.a.charlee.reader;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.TableResult;
import company.a.charlee.repository.ProcessedFileRepository;
import company.a.charlee.services.SocialMediaParquetProcessor;

import java.io.FileInputStream;
import java.io.IOException;

public abstract class AbstractBigQueryParquetReaderService {

    protected final SocialMediaParquetProcessor processor;
    protected final BigQuery bigQuery;
    protected final ProcessedFileRepository processedFileRepository;



    protected AbstractBigQueryParquetReaderService(SocialMediaParquetProcessor processor, BigQuery bigQuery, ProcessedFileRepository processedFileRepository) {
        this.processor = processor;
        this.bigQuery = bigQuery;
        this.processedFileRepository = processedFileRepository;
    }
    private BigQuery createBigQuery(String credentialsFilePath) throws IOException {
        return BigQueryOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(credentialsFilePath)))
                .build()
                .getService();
    }
    protected abstract String getMediaType();

    public void checkAndProcessNewFiles() throws InterruptedException {
    }
    protected void readAndProcessParquetFiles(TableResult result) {
        processor.processBigQueryResult(result);
    }

}