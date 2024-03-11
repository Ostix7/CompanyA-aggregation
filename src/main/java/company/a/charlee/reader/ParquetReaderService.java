package company.a.charlee.reader;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ParquetReaderService {

    private SparkSession sparkSession;

    public ParquetReaderService() {
        sparkSession = SparkSession.builder()
                .appName("Parquet Reader")
                .master("local[*]")
                .getOrCreate();
    }

    @Scheduled(fixedRate = 3600000) //hour
    public void readParquetFiles() {
        Dataset<Row> parquetFileDF = sparkSession.read().parquet("s3a://bucket-name/path/to/parquet/file");
        
        parquetFileDF.printSchema();
        
        parquetFileDF.show();
    }
}