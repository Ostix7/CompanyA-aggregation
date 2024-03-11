package company.a.charlee.config;

import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparkConfig {

    @Bean
    public SparkSession sparkSession() {
        return SparkSession.builder()
                .appName("Parquet Reader from S3")
                .master("local[*]")
                .config("spark.hadoop.fs.s3a.impl", "org.apache.hadoop.fs.s3a.S3AFileSystem")
                .config("spark.hadoop.fs.s3a.access.key", "<our-team-access-key>")
                .config("spark.hadoop.fs.s3a.secret.key", "<our-team-secret-key>")
                .config("spark.hadoop.fs.s3a.multipart.size", "104857600")
                .getOrCreate();
    }
}