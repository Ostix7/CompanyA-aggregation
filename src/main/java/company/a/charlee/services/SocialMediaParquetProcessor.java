package company.a.charlee.services;

import com.google.cloud.bigquery.TableResult;

public interface SocialMediaParquetProcessor {
    void processBigQueryResult(TableResult result);
}