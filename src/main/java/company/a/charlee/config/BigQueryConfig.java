package company.a.charlee.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class BigQueryConfig {

    private final AmazonS3 amazonS3Client;

    public BigQueryConfig(AmazonS3 amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Bean("bigQueryForTelegram")
    public BigQuery bigQueryForTelegram() throws IOException {
        S3Object s3Object = amazonS3Client.getObject("creds-aggregation-team", "telegram.json");
        InputStream objectData = s3Object.getObjectContent();

        return BigQueryOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(objectData))
                .build()
                .getService();
    }

    @Bean("bigQueryForYoutube")
    public BigQuery bigQueryForYoutube() throws IOException {
        S3Object s3Object = amazonS3Client.getObject("creds-aggregation-team", "youtube.json");
        InputStream objectData = s3Object.getObjectContent();

        return BigQueryOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(objectData))
                .build()
                .getService();
    }
}