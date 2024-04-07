package company.a.charlee.config;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
public class BigQueryConfig {

    @Bean("bigQueryForTelegram")
    public BigQuery bigQueryForTelegram() throws IOException {
        ClassPathResource resource = new ClassPathResource("bigquery/telegram.json");

        return BigQueryOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(resource.getInputStream()))
                .build()
                .getService();
    }

    @Bean("bigQueryForYoutube")
    public BigQuery bigQueryForYoutube() throws IOException {
        ClassPathResource resource = new ClassPathResource("bigquery/youtube.json");

        return BigQueryOptions.newBuilder()
                .setCredentials(ServiceAccountCredentials.fromStream(resource.getInputStream()))
                .build()
                .getService();
    }
}